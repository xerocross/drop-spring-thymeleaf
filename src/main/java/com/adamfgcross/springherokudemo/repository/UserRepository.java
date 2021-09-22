package com.adamfgcross.springherokudemo.repository;

import com.adamfgcross.springherokudemo.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
