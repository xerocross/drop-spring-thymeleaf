package com.adamfgcross.drop.service;

import com.adamfgcross.drop.entity.User;
import com.adamfgcross.drop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Boolean checkUser(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            return userOpt.get().getUsername().equals(password);
        } else {
            return false;
        }
    }

    public void populateInitialDropsForNewUser(User user) {

    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

}
