package com.adamfgcross.drop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelpController {

    @GetMapping("/about")
    public String instructions(Model model) {
        return "about";
    }

}
