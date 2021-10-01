package com.adamfgcross.drop.service;

import com.adamfgcross.drop.entity.User;
import com.adamfgcross.drop.exception.UserAlreadyExistsException;
import com.adamfgcross.drop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    private void encodeUserPassword(User user) {
        String plaintextPassword = user.getPassword();
        String encodedPassword = passwordEncoder.encode(plaintextPassword);
        user.setPassword(encodedPassword);
    }

    @Override
    public User attemptSaveNewUser(User newUser) {
        Optional<User> foundUserOptional = findByUsername(newUser.getUsername());
        foundUserOptional.ifPresent((user) -> {
            throw new UserAlreadyExistsException();
        });
        // user not found, so we may create a new user
        encodeUserPassword(newUser);
        return userRepository.save(newUser);
    }

}
