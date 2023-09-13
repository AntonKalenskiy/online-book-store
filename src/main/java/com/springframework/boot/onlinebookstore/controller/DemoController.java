package com.springframework.boot.onlinebookstore.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/")
    public String sayHello(Authentication authentication) {
        String name = authentication.getName();
        return "Hello";
    }
}
