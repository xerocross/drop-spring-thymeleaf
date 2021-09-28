package com.adamfgcross.springherokudemo.service;


import com.adamfgcross.springherokudemo.entity.Drop;
import com.adamfgcross.springherokudemo.entity.User;
import com.adamfgcross.springherokudemo.repository.DropRepository;
import com.adamfgcross.springherokudemo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DropServiceImpl implements DropService {
    private final DropRepository dropRepository;
    private final UserRepository userRepository;

    public DropServiceImpl(DropRepository dropRepository, UserRepository userRepository) {
        this.dropRepository = dropRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveDrop(Drop drop) {
        dropRepository.save(drop);
    }

    @Override
    public void saveDrop(String dropText, User user) {
        Drop drop = new Drop();
        drop.setText(dropText);
        drop.setUser(user);
        dropRepository.save(drop);
    }

    @Override
    public List<Drop> getDrops(String query, String username) {
        List<Drop> drops = dropRepository.findByTextContains(query);
        return drops;
    }

    @Override
    public List<Drop> getAllDrops(Long userId) {
        List<Drop> drops = new ArrayList<>();
        for (Drop drop : dropRepository.findByUser_Id(userId)) {
            drops.add(drop);
        }
        return drops;
    }

    @Override
    public List<Drop> getAllDrops() {
        List<Drop> drops = new ArrayList<>();
        for (Drop drop : dropRepository.findAll()) {
            drops.add(drop);
        }
        return drops;
    }

    @Override
    public Boolean removeDrop(Long dropId) {
        Optional<Drop> dropOptional = dropRepository.findById(dropId);
        if (dropOptional.isPresent()) {
            try {
                dropRepository.delete(dropOptional.get());
                return true;
            }
            catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public List<Drop> queryDrops(String query, String username) {



        return null;
    }
}
