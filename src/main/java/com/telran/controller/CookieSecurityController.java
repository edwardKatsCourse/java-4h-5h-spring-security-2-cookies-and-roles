package com.telran.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

@RestController
@RequestMapping("/secured")
public class CookieSecurityController {


    //create
    @PostMapping("/cookies/{name}")
    public void getCookie(@PathVariable("name") String name,
                          HttpServletRequest request,
                          HttpServletResponse response) {

        //"Server" cookie - has an encrypted value and client cannot read it

        HttpSession session = request.getSession();

        //Attribute - Map<String, Object>
        session.setAttribute("name", name);
    }

    @GetMapping("/cookies")
    public String getNameFromCookies(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Object serverCookie = session.getAttribute("name");
        if (serverCookie != null) {
            return serverCookie.toString();
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/cookies")
    public void deleteCookie(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.removeAttribute("name");
    }

}
