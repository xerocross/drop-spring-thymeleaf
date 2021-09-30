package com.adamfgcross.drop.repository;

import com.adamfgcross.drop.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
