package com.telran.controller;

import com.telran.dto.PersonResponse;
import com.telran.entity.Person;
import com.telran.entity.PersonSession;
import com.telran.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PersonController {

    @Autowired
    private PersonRepository personRepository;

    //Employee
    @GetMapping("/persons/{id}")
    public PersonResponse getPersonInfo(@PathVariable("id") Long id, @AuthenticationPrincipal PersonSession personSession) {

        Person person = personSession.getPerson();
        if (!person.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to call this method");
        }

        return PersonResponse.builder()
                .id(person.getId())
                .createdDate(person.getCreatedDate())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .build();
    }

    //Admin
    @Transactional
    @PutMapping("/admin/persons/{id}")
    public void updatePerson(@RequestBody Person person, @PathVariable("id") Long personId) {
        Person dbPerson = personRepository.findById(personId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.GONE, "The requested person does not exist"));

        if (person.getFirstName() != null) {

            dbPerson.setFirstName(person.getFirstName());
        }

        if (person.getLastName() != null) {
            dbPerson.setLastName(person.getLastName());
        }

        if (person.getUsername() != null && !person.getUsername().equals(dbPerson.getUsername())) {
            boolean exists = personRepository.existsByUsername(person.getUsername());
            if (exists) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot modify username. Such username already exists");
            }

            dbPerson.setUsername(person.getUsername());
        }
    }

    //Admin
    @GetMapping("/admin/persons")
    public List<PersonResponse> getPersons() {

        return personRepository.findAll()
                .stream()
                .map(x -> PersonResponse.builder()
                        .firstName(x.getFirstName())
                        .lastName(x.getLastName())
                        .createdDate(x.getCreatedDate())
                        .id(x.getId())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
