package com.adamfgcross.drop.service;

import com.adamfgcross.drop.entity.Drop;
import com.adamfgcross.drop.entity.User;

import java.util.List;
import java.util.Optional;

public interface DropService {
    public void saveDrop(Drop drop);

    public void saveDrop(String dropText, User user);

    public Boolean save (Drop drop);

    public List<Drop> getDrops(String dropText, String username);

    public List<Drop> queryDrops(String query, String username);

    public List<Drop> getAllDrops(Long userId);

    public Boolean removeDrop(Long dropId);

    public Optional<Drop> findById(Long id);

    public List<Drop> getAllDrops();

    public Boolean updateDrop(Drop drop, User user);

    public Boolean deleteDrop(Drop drop, User authUser);
}
