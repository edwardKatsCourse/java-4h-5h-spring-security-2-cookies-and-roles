package com.telran.repository;

import com.telran.entity.PersonSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonSessionRepository extends JpaRepository<PersonSession, Long> {

    PersonSession findBySessionId(String sessionId);
    boolean existsBySessionId(String sessionId);
    void deleteBySessionId(String sessionId);
}
