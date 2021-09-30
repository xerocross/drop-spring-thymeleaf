package com.adamfgcross.drop.controller;

import com.adamfgcross.drop.entity.Drop;
import com.adamfgcross.drop.entity.User;
import com.adamfgcross.drop.exception.ResourceNotFoundException;
import com.adamfgcross.drop.exception.UserNotFoundException;
import com.adamfgcross.drop.service.DropService;
import com.adamfgcross.drop.service.QueryService;
import com.adamfgcross.drop.service.UserService;
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

    private final DropService dropService;
    private final QueryService queryService;
    private final UserService userService;

    @Autowired
    public DropFormController(DropService dropService, QueryService queryService, UserService userService) {
        this.dropService = dropService;
        this.queryService = queryService;
        this.userService = userService;
    }

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
    public String postDrop(Authentication authentication, @Valid Drop drop, BindingResult bindingResult, Model model, RedirectAttributes redirAttrs) {
        if (bindingResult.hasErrors()) {
            System.out.println("Errors:");
            System.out.println(bindingResult.getAllErrors());
            return "drop-form";
        }
        Optional<User> userOptional = getUser(authentication);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException();
        }
        User user = userOptional.get();
        if (dropService.updateDrop(drop, user)) {
            redirAttrs.addFlashAttribute("messageText", "Drop saved successfully");
        } else {
            redirAttrs.addFlashAttribute("errorText", "An unknown error occurred. Please try again.");
        }
        return "redirect:/drop";
    }

    @DeleteMapping("/drop/{id}")
    public ResponseEntity<Long> deleteDrop(@PathVariable Long id, Authentication authentication) {
        Optional<User> userOptional =  getUser(authentication);
        Optional<Drop> dropOptional = dropService.findById(id);
        if (!userOptional.isPresent()) {
            throw new UserNotFoundException();
        }
        if (!dropOptional.isPresent()) {
            throw new ResourceNotFoundException();
        }
        Boolean result = dropService.deleteDrop(dropOptional.get(), userOptional.get());
        return result ? ResponseEntity.ok(id) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
