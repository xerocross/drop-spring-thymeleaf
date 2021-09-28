package com.adamfgcross.springherokudemo.controller;


import com.adamfgcross.springherokudemo.entity.User;
import com.adamfgcross.springherokudemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("register")
    public String getRegistrationForm( User user, Model model) {
        return "registration-form";
    }

    @PostMapping("register")
    public String register(Authentication authentication, @Valid User user, BindingResult bindingResult, Model model) {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        SecurityContextHolder.clearContext();
        if (bindingResult.hasErrors()) {
            System.out.println("Errors:");
            System.out.println(bindingResult.getAllErrors());
            return "registration-form";
        }
        Optional<User> userOptional = userService.findByUsername(user.getUsername());
        if (userOptional.isPresent()) {
            model.addAttribute("errorText", "User already exists; try again.");
            return "registration-form";
        } else {
            User newUser = user;
            // encode password
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
            try {
                userService.save(newUser);
                return "redirect:/drop";
            } catch (Exception e) {
                model.addAttribute("errorText", "An unknown error occurred; try again.");
                return "registration-form";
            }
        }
    }
}

