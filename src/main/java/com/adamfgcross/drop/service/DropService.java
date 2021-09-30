package com.adamfgcross.drop.service;

import com.adamfgcross.drop.entity.Drop;
import com.adamfgcross.drop.entity.User;

import java.util.Optional;

public interface DropService {
    Boolean save (Drop drop);
    Optional<Drop> findById(Long id);
    Boolean updateDrop(Drop drop, User user);
    Boolean deleteDrop(Drop drop, User authUser);
}
