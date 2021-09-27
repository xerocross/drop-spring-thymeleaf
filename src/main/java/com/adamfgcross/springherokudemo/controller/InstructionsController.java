package com.adamfgcross.springherokudemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InstructionsController {

    @GetMapping("/")
    public String instructions(Model model) {
        return "hello";
    }

}
