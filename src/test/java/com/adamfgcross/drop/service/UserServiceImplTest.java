package com.adamfgcross.drop.service;

import com.adamfgcross.drop.entity.User;
import com.adamfgcross.drop.exception.UserAlreadyExistsException;
import com.adamfgcross.drop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class UserServiceImplTest {

    private UserServiceImpl userService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void canCreateUserService() {
        // beforeEach runs successfully
    }

    @Test
    void canAddUserWhenNoSuchUsernameFound() {
        User user = new User();
        user.setUsername("adam");
        user.setPassword("password");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        userService.attemptSaveNewUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void encodesPasswordWhenSavingNewUser() {
        User user = new User();
        user.setUsername("adam");
        user.setPassword("password");
        String encodedPassword = "encoded";
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        User returnedUser = userService.attemptSaveNewUser(user);
        assert(returnedUser.getPassword()).equals(encodedPassword);
    }

    @Test
    void throwsIfUserAlreadyExists() {
        User user = new User();
        user.setUsername("adam");
        user.setPassword("password");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.attemptSaveNewUser(user);
        });
        verify(userRepository, times(0)).save(user);
    }
}
