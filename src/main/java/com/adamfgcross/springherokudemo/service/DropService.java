package com.adamfgcross.springherokudemo.service;

import com.adamfgcross.springherokudemo.entity.Drop;
import com.adamfgcross.springherokudemo.entity.User;

import java.util.List;

public interface DropService {
    public void saveDrop(Drop drop);

    public void saveDrop(String dropText, User user);

    public List<Drop> getDrops(String dropText, String username);

    public List<Drop> queryDrops(String query, String username);

    public List<Drop> getAllDrops(Long userId);

    public Boolean removeDrop(Long dropId);

    public List<Drop> getAllDrops();
}
