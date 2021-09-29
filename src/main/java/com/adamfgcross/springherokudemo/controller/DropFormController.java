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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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
        return "redirect:/drop";
    }

    @GetMapping("/drop/{id}")
    public String getDrop(@PathVariable Long id, Model model) {
        Optional<Drop> dropOptional = dropService.findById(id);
        Drop drop;
        if (dropOptional.isPresent()) {
            drop = dropOptional.get();
            model.addAttribute("update",true);

        } else {
            throw new RuntimeException("could not find drop");
        }
        model.addAttribute("drop", drop);
        return "drop-form";
    }

    @PostMapping("/drop")
    public String postDrop2(Authentication authentication, @Valid Drop drop, BindingResult bindingResult, Model model, RedirectAttributes redirAttrs) {
        if (bindingResult.hasErrors()) {
            System.out.println("Errors:");
            System.out.println(bindingResult.getAllErrors());
            return "drop-form";
        }

        String dropText = drop.getText();
        Optional<User> userOptional = getUser(authentication);
        System.out.println("received dropText" + dropText);
        if (!userOptional.isPresent()) {
            redirAttrs.addFlashAttribute("errorText", "User not found in database");
        } else {
            User user = userOptional.get();
            drop.setUser(user);
            dropService.saveDrop(drop);
            //dropService.saveDrop(dropText, user);
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
    public String getDropsView(Authentication authentication, Drop drop, @RequestParam(value = "query", defaultValue = "") String query, Model model) {
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
