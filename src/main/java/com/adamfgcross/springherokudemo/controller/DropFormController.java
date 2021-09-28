package com.adamfgcross.springherokudemo.controller;

import com.adamfgcross.springherokudemo.entity.Drop;
import com.adamfgcross.springherokudemo.entity.User;
import com.adamfgcross.springherokudemo.service.DropService;
import com.adamfgcross.springherokudemo.service.QueryService;
import com.adamfgcross.springherokudemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String postDrop(Authentication authentication, @RequestParam(value = "dropText", defaultValue = "") String dropText, Model model, RedirectAttributes redirAttrs) {
        Optional<User> userOptional = getUser(authentication);
        System.out.println("received dropText" + dropText);
        if (!userOptional.isPresent()) {
            redirAttrs.addFlashAttribute("errorText", "User not found in database");
        } else {
            User user = userOptional.get();
            dropService.saveDrop(dropText, user);
            redirAttrs.addFlashAttribute("messageText", "Drop saved successfully");
        }
        return "redirect:/drop";
    }

    @DeleteMapping("/drop/{id}")
    public ResponseEntity<Long> deleteDrop(@PathVariable Long id) {
        if (dropService.removeDrop(id)) {
            return ResponseEntity.ok(id);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
        model.addAttribute("query", query);
        return "drop-form";
    }

    private  Optional<User> getUser(Authentication authentication) {
        String username = authentication.getName();
        return userService.findByUsername(username);
    }


}
