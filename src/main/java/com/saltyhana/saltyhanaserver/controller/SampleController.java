package com.saltyhana.saltyhanaserver.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sample")
public class SampleController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}
