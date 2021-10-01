package com.adamfgcross.drop.service;


import com.adamfgcross.drop.entity.User;

import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    void save(User user);
    User attemptCreateNewUser(User newUser);
}
