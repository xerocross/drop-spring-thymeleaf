package com.adamfgcross.drop.service;


import com.adamfgcross.drop.entity.User;

import java.util.Optional;

public interface UserService {

    public Boolean checkUser(String username, String password);

    public Optional<User> findByUsername(String username);

    public void save(User user);
}
