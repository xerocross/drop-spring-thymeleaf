package com.adamfgcross.springherokudemo.service;


import com.adamfgcross.springherokudemo.entity.User;

import java.util.Optional;

public interface UserService {

    public Boolean checkUser(String username, String password);

    public Optional<User> findByUsername(String username);
}
