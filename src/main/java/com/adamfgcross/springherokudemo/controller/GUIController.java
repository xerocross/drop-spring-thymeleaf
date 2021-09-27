package com.adamfgcross.springherokudemo.controller;


import com.adamfgcross.springherokudemo.entity.Drop;
import com.adamfgcross.springherokudemo.entity.User;
import com.adamfgcross.springherokudemo.service.QueryService;
import com.adamfgcross.springherokudemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class GUIController {

    @Autowired
    private UserService userService;

    @Autowired
    private QueryService queryService;

    @GetMapping("dropText")
    public String getDropText() {
        return "drop";
    }

//    @GetMapping("query")
//    public String getQueryForm() {
//        return "query";
//    }
//    private  Optional<User> getUser(Authentication authentication) {
//        String username = authentication.getName();
//        return userService.findByUsername(username);
//    }
//    @GetMapping("drops-view")
//    public String getDropsView(Authentication authentication, @RequestParam(value = "query", defaultValue = "") String query, Model model) {
//        Optional<User> userOptional = getUser(authentication);
//        List<Drop> drops;
//        if (userOptional.isPresent()) {
//            Long id = userOptional.get().getId();
//            drops = queryService.queryDrops(query, id);
//        } else {
//            drops = Collections.emptyList();
//        }
//        model.addAttribute("drops", drops);
//        return "drops";
//    }
}
