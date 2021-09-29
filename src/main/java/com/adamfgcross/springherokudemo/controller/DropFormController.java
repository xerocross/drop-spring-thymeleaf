package com.adamfgcross.springherokudemo.controller;

import com.adamfgcross.springherokudemo.entity.Drop;
import com.adamfgcross.springherokudemo.entity.User;
import com.adamfgcross.springherokudemo.exception.BadRequestException;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class DropFormController {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class ResourceNotFoundException extends RuntimeException {
    }

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
    public String getDrop(@PathVariable Long id, Model model, HttpServletResponse httpServletResponse) {
        Optional<Drop> dropOptional = dropService.findById(id);
        Drop drop;
        if (dropOptional.isPresent()) {
            drop = dropOptional.get();
            model.addAttribute("update",true);

        } else {
            throw new ResourceNotFoundException();
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
        // check if post is an update
        Boolean isUpdate = false;
        if (drop.getId() != null) {
            isUpdate = true;
        }


        String dropText = drop.getText();
        Optional<User> userOptional = getUser(authentication);
        System.out.println("received dropText: " + dropText);
        if (!userOptional.isPresent()) {
            // this should never happen
            redirAttrs.addFlashAttribute("errorText", "User not found in database");
        } else {
            User user = userOptional.get();
            if (isUpdate) {
                Long dropId = drop.getId();
                Long userId = user.getId();
                Optional<Drop> existingDrop = dropService.findById(dropId);
                if (!existingDrop.isPresent()) {
                    throw new BadRequestException();
                } else {
                    if (existingDrop.get().getUser().getId().equals(userId)) {
                        // user is authorized
                        drop.setUser(user);
                        dropService.saveDrop(drop);
                        redirAttrs.addFlashAttribute("messageText", "Drop saved successfully");
                    } else {
                        // edit/update was unauthorized
                        redirAttrs.addFlashAttribute("errorText", "An error occurred. Please try again.");
                    }
                }
            } else {
                drop.setUser(user);
                dropService.saveDrop(drop);
                redirAttrs.addFlashAttribute("messageText", "Drop saved successfully");
            }
        }
        return "redirect:/drop";
    }

    @DeleteMapping("/drop/{id}")
    public ResponseEntity<Long> deleteDrop(@PathVariable Long id, Authentication authentication) {
        Optional<User> userOptional =  getUser(authentication);
        Optional<Drop> dropOptional = dropService.findById(id);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("user not found");
        }
        if (!dropOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Drop drop = dropOptional.get();
        User user = userOptional.get();
        Long userId = user.getId();
        // check if the drop belongs to the user
        if (drop.getUser().getId() != userId) {
            // if not, return forbidden
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
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
