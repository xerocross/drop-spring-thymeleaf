package com.adamfgcross.springherokudemo.bootstrap;

import com.adamfgcross.springherokudemo.entity.Drop;
import com.adamfgcross.springherokudemo.entity.User;
import com.adamfgcross.springherokudemo.repository.DropRepository;
import com.adamfgcross.springherokudemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class DataBootstrap implements ApplicationListener<ContextRefreshedEvent> {


    private final UserRepository userRepository;
    private final DropRepository dropRepository;


    @Value("${app.config.mainuser.username}")
    private String mainUsername;
    @Value("${app.config.mainuser.password}")
    private String mainPassword;

    public DataBootstrap(UserRepository userRepository, DropRepository dropRepository) {
        this.userRepository = userRepository;
        this.dropRepository = dropRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        User adam = new User();
        adam.setUsername(mainUsername);
        adam.setPassword(mainPassword);
        userRepository.save(adam);

        Drop drop = new Drop();
        drop.setText("test #apple");
        drop.setUser(adam);
        dropRepository.save(drop);

        drop = new Drop();
        drop.setText("test #pear");
        drop.setUser(adam);
        dropRepository.save(drop);

        drop = new Drop();
        drop.setText("test #pear #apple");
        drop.setUser(adam);
        dropRepository.save(drop);
    }
}
