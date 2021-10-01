package com.adamfgcross.drop.service;

import com.adamfgcross.drop.bootstrap.BootstrapDrops;
import com.adamfgcross.drop.entity.Drop;
import com.adamfgcross.drop.entity.User;
import com.adamfgcross.drop.exception.UserAlreadyExistsException;
import com.adamfgcross.drop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class UserServiceImplTest {

    private UserServiceImpl userService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private BootstrapDrops bootstrapDrops;
    private DropService dropService;
    private Drop testDrop;

    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        createBootstrapDrops();
        dropService = mock(DropService.class);
        userService = new UserServiceImpl(userRepository, passwordEncoder, bootstrapDrops, dropService);
    }

    private void createBootstrapDrops() {
        bootstrapDrops = new BootstrapDrops();
        testDrop = new Drop();
        testDrop.setText("test");
        bootstrapDrops.setDrops(new ArrayList<>());
        bootstrapDrops.getDrops().add(testDrop);
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
        userService.attemptCreateNewUser(user);
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
        User returnedUser = userService.attemptCreateNewUser(user);
        assert(returnedUser.getPassword()).equals(encodedPassword);
    }

    @Test
    void throwsIfUserAlreadyExists() {
        User user = new User();
        user.setUsername("adam");
        user.setPassword("password");
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));
        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.attemptCreateNewUser(user);
        });
        verify(userRepository, times(0)).save(user);
    }

    @Test
    void creatingNewUserBootstrapsDrops() {
        User user = new User();
        user.setUsername("adam");
        user.setPassword("password");
        String encodedPassword = "encoded";
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPassword);
        userService.attemptCreateNewUser(user);
        assert(testDrop.getUser()).equals((user));
    }
}
