package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AuthController {

    @GetMapping
    public String getIndexPage(){
        return "index";
    }

    @GetMapping("login")
    public String loginPage() {
        return "login";
    }
}
