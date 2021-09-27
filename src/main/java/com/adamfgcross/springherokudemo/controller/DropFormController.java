package com.adamfgcross.springherokudemo.controller;

import com.adamfgcross.springherokudemo.entity.Drop;
import com.adamfgcross.springherokudemo.entity.User;
import com.adamfgcross.springherokudemo.service.DropService;
import com.adamfgcross.springherokudemo.service.QueryService;
import com.adamfgcross.springherokudemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class DropFormController {

    @Autowired
    private DropService dropService;
    @Autowired
    private QueryService queryService;
    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String helloWorld() {
        return "helloworld";
    }

    @PostMapping("/drop")
    public String postDrop(Authentication authentication, @RequestParam(value = "dropText", defaultValue = "") String dropText, Model model) {
        Optional<User> userOptional = getUser(authentication);
        System.out.println("received dropText" + dropText);
        if (!userOptional.isPresent()) {
            model.addAttribute("errorText", "User not found in database");
        } else {
            User user = userOptional.get();
            dropService.saveDrop(dropText, user);
            model.addAttribute("messageText", "Drop saved successfully");
        }
            return "drop-form";
    }

    @GetMapping("drop")
    public String getDropsView(Authentication authentication, @RequestParam(value = "query", defaultValue = "") String query, Model model) {
        Optional<User> userOptional = getUser(authentication);
        List<Drop> drops;
        if (userOptional.isPresent()) {
            Long id = userOptional.get().getId();
            drops = queryService.queryDrops(query, id);
        } else {
            drops = Collections.emptyList();
        }
        model.addAttribute("drops", drops);
        return "drop-form";
    }

    private  Optional<User> getUser(Authentication authentication) {
        String username = authentication.getName();
        return userService.findByUsername(username);
    }


}
