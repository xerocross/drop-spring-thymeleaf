package com.adamfgcross.springherokudemo.controller;


import com.adamfgcross.springherokudemo.entity.Drop;
import com.adamfgcross.springherokudemo.entity.User;
import com.adamfgcross.springherokudemo.service.DropService;
import com.adamfgcross.springherokudemo.service.QueryService;
import com.adamfgcross.springherokudemo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class DropController {


    private final DropService dropService;
    private final QueryService queryService;
    private final UserService userService;

    public DropController(DropService dropService, QueryService queryService, UserService userService) {
        this.dropService = dropService;
        this.queryService = queryService;
        this.userService = userService;
    }

    @PostMapping("/drop")
    public ResponseEntity<String> postDrop(Authentication authentication, @RequestParam(value = "dropText", defaultValue = "") String dropText) {
        Optional<User> userOptional = getUser(authentication);
        System.out.println("received dropText" + dropText);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("user not found in database");
        } else {
            User user = userOptional.get();
            dropService.saveDrop(dropText, user);
            return ResponseEntity.ok("Saved");
        }
    }


    private  Optional<User> getUser(Authentication authentication) {
        String username = authentication.getName();
        return userService.findByUsername(username);
    }

    @GetMapping("/drops")
    public ResponseEntity<List<Drop>> queryDrops(Authentication authentication, @RequestParam(value = "query", defaultValue = "") String query) {
        Optional<User> userOptional = getUser(authentication);
        if (userOptional.isPresent()) {
            Long id = userOptional.get().getId();
            List<Drop> drops = queryService.queryDrops(query, id);
            return ResponseEntity.ok(drops);
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }
}
