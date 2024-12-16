package com.saltyhana.saltyhanaserver.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sample")
public class SampleController {

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }

    @GetMapping("/hi")
    public String hi() {
        return "Hi World!";
    }
}
