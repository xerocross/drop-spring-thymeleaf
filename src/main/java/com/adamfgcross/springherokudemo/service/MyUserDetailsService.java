package com.adamfgcross.springherokudemo.service;

import com.adamfgcross.springherokudemo.entity.User;
import com.adamfgcross.springherokudemo.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> userOptional =  userRepository.findByUsername(s);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(s);
        } else {
            User myUser = userOptional.get();
            return new MyUserPrincipal(myUser);
        }
    }
}
