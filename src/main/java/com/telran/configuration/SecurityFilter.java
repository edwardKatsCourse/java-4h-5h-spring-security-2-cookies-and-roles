package com.telran.configuration;

import com.telran.entity.PersonSession;
import com.telran.repository.PersonSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private PersonSessionRepository personSessionRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,

            FilterChain filterChain) throws ServletException, IOException {

//        String header = httpServletRequest.getHeader("Authorization");
        String header = Optional
                .ofNullable(httpServletRequest.getSession(false))
                .map(x -> x.getAttribute("sessionId"))
                .map(Object::toString)
                .orElse(null);

        if (header != null) {

            PersonSession personSession = personSessionRepository.findBySessionId(header);
            if (personSession != null) {

                Authentication key = new UsernamePasswordAuthenticationToken(
                        personSession,
                        null,
                        personSession.getPerson().getRoles()
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
                );

                SecurityContextHolder.getContext().setAuthentication(key);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
