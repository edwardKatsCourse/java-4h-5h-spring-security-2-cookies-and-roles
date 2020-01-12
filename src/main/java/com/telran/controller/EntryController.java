package com.telran.controller;

import com.telran.dto.PersonRequest;
import com.telran.entity.Person;
import com.telran.entity.PersonSession;
import com.telran.repository.PersonRepository;
import com.telran.repository.PersonSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@RestController
public class EntryController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonSessionRepository personSessionRepository;

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/register")
    public void register(@RequestBody PersonRequest personRequest) {
        boolean personExists = personRepository.existsByUsername(personRequest.getUsername());
        if (personExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Person with such username already exists");
        }

        Person person = Person.builder()
                .createdDate(LocalDateTime.now())
                .username(personRequest.getUsername())
                .password(personRequest.getPassword())
                .firstName(personRequest.getUsername())
                .lastName(personRequest.getUsername())
                .roles(Arrays.asList("EMPLOYEE"))
                .build();

        personRepository.save(person);
    }

    @PostMapping("/login")
    public void login(@RequestBody PersonRequest personRequest, HttpServletRequest request) {
        Person person = personRepository.findByUsernameAndPassword(
                personRequest.getUsername(),
                personRequest.getPassword()
        );

        if (person == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is incorrect");
        }

        PersonSession personSession = PersonSession.builder()
                .person(person)
                .sessionId(UUID.randomUUID().toString())
                .build();

        personSessionRepository.save(personSession);

        request.getSession(true).setAttribute("sessionId", personSession.getSessionId());
    }


    @PutMapping("/logout")
    @Transactional
    public void logout(@AuthenticationPrincipal PersonSession personSession, HttpServletRequest request) {
        personSessionRepository.deleteBySessionId(personSession.getSessionId());

        request.getSession().removeAttribute("sessionId");
    }


}
