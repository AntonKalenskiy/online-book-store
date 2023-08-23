package com.springframework.boot.onlinebookstore.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/")
    public String showHello() {
        return "hello guys";
    }

    @GetMapping("/bye")
    public String showBye() {
        return "Bye guys";
    }
}
