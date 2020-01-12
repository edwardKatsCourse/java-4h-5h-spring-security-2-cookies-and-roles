package com.telran.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@RestController
public class CookieController {

//    @Autowired
//    private HttpServletRequest request;

    //create
    @PostMapping("/cookies-example/{name}")
    public void getCookie(@PathVariable("name") String name,
                          HttpServletRequest request,
                          HttpServletResponse response) {

        Cookie cookie = new Cookie("name", name);
        response.addCookie(cookie);
    }

    @GetMapping("/cookies-example")
    public String getNameFromCookies(HttpServletRequest request) {
        Cookie nameCookie = getCookieFromRequest(request);

        return nameCookie == null ? "No cookies with name 'name'" : nameCookie.getValue();
    }

    @DeleteMapping("/cookies-example")
    public void deleteCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = getCookieFromRequest(request);
        if (cookie != null) {
            cookie.setValue(null);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

    private Cookie getCookieFromRequest(HttpServletRequest request) {
        return Arrays.asList(request.getCookies())
                .stream()
                .filter(x -> x.getName().equals("name"))
                .findFirst()
                .orElse(null);
    }
}
