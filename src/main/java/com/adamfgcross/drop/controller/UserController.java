package com.adamfgcross.drop.controller;


import com.adamfgcross.drop.entity.User;
import com.adamfgcross.drop.exception.UserAlreadyExistsException;
import com.adamfgcross.drop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String viewLoginPage(User user) {
        return "login";
    }

    @GetMapping("register")
    public String getRegistrationForm( User user, Model model) {
        return "registration-form";
    }


    private void logoutProgrammatically() {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
        SecurityContextHolder.clearContext();
    }

    @PostMapping("register")
    public String register(@Valid User user, BindingResult bindingResult, Model model) {
        logoutProgrammatically();
        if (bindingResult.hasErrors()) {
            return "registration-form";
        }
        try {
            userService.attemptCreateNewUser(user);
            return "redirect:/drop";
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("errorText", "User already exists; try again.");
            return "registration-form";
        }
    }
}