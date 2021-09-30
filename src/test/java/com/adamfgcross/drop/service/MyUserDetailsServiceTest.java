package com.adamfgcross.drop.service;

import com.adamfgcross.drop.entity.User;
import com.adamfgcross.drop.exception.UserNotFoundException;
import com.adamfgcross.drop.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MyUserDetailsServiceTest {

    @Test
    public void canCreateMyUserDetailsService() {
        UserRepository userRepository = mock(UserRepository.class);
        UserDetailsService userDetailsService = new MyUserDetailsService(userRepository);
    }

    @Test
    public void canGetUserDetailsIfUserFound_username() {
        UserRepository userRepository = mock(UserRepository.class);
        UserDetailsService userDetailsService = new MyUserDetailsService(userRepository);
        String username = "adam";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assert(userDetails.getUsername()).equals(user.getUsername());
    }

    @Test
    public void canGetUserDetailsIfUserFound_password() {
        UserRepository userRepository = mock(UserRepository.class);
        UserDetailsService userDetailsService = new MyUserDetailsService(userRepository);
        String password = "password";
        String username = "adam";
        User user = new User();
        user.setPassword(password);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assert(userDetails.getPassword()).equals(user.getPassword());
    }

    @Test
    public void throwsIfUserNotFound() {
        UserRepository userRepository = mock(UserRepository.class);
        UserDetailsService userDetailsService = new MyUserDetailsService(userRepository);
        String username = "adam";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });
    }
}
