package com.telran.runner;

import com.telran.entity.Person;
import com.telran.repository.PersonRepository;
import com.telran.repository.PersonSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class ApplicationRunner implements CommandLineRunner {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void run(String... args) throws Exception {
        Person person_1 = Person.builder()
                .username("john")
                .password("123456")
                .firstName("John")
                .lastName("Smith")
                .roles(Arrays.asList("ADMIN", "EMPLOYEE"))
                .createdDate(LocalDateTime.now())
                .build();

        personRepository.save(person_1);

        Person person_2 = Person.builder()
                .createdDate(LocalDateTime.now())
                .username("anna")
                .firstName("Anna")
                .lastName("Dale")
                .password("123456")
                .roles(Arrays.asList("EMPLOYEE"))
                .build();

        personRepository.save(person_2);
    }
}
