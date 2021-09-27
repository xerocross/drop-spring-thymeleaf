package com.adamfgcross.springherokudemo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @Value("${config.hello}")
    private String configHello;

    @GetMapping("/hello")
    public ResponseEntity<String> getHello() {
        return ResponseEntity.ok("Hello World");
    }
    @GetMapping("/secret")
    public ResponseEntity<String> getSecret() {
        return ResponseEntity.ok("Shhh. It's a secret!");
    }
    @GetMapping("/")
    public ResponseEntity<String> getBaseGreeting() {
        return ResponseEntity.ok(configHello);
    }



}
