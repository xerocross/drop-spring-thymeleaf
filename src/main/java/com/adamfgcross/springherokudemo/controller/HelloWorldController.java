package com.adamfgcross.springherokudemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/hello")
    public ResponseEntity<String> getHello() {
        return ResponseEntity.ok("Hello World");
    }
    @GetMapping("/secret")
    public ResponseEntity<String> getSecret() {
        return ResponseEntity.ok("Shhh. It's a secret!");
    }
}
