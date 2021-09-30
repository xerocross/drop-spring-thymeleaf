package com.adamfgcross.drop.service;


import com.adamfgcross.drop.entity.Drop;
import com.adamfgcross.drop.entity.User;
import com.adamfgcross.drop.exception.BadRequestException;
import com.adamfgcross.drop.exception.ForbiddenRequestException;
import com.adamfgcross.drop.repository.DropRepository;
import com.adamfgcross.drop.repository.UserRepository;
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
    public Optional<Drop> findById(Long id) {
        return dropRepository.findById(id);
    }

    @Override
    public void saveDrop(Drop drop) {
        dropRepository.save(drop);
    }

    @Override
    public Boolean save (Drop drop) {
        return drop.equals(dropRepository.save(drop));
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

    public Boolean updateDrop(Drop drop, User user) {
        // check if post is an update
        Boolean isUpdate = false;
        if (drop.getId() != null) {
            isUpdate = true;
        }
        if (isUpdate) {
            Long dropId = drop.getId();
            Long userId = user.getId();
            Optional<Drop> existingDrop = findById(dropId);
            if (!existingDrop.isPresent()) {
                throw new BadRequestException();
            } else {
                if (existingDrop.get().getUser().getId().equals(userId)) {
                    // user is authorized
                    drop.setUser(user);
                    return save(drop);
                } else {
                    throw new ForbiddenRequestException();
                }
            }
        } else {
            drop.setUser(user);
            return save(drop);
        }
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

    private Boolean isDropBelongsToUser(Drop drop, User user) {
        Long dropId = drop.getId();
        Long userId = user.getId();
        Optional<Drop> foundDropOptional = findById(dropId);
        if (!foundDropOptional.isPresent()) {
            throw new BadRequestException();
        } else {
            Drop foundDrop = foundDropOptional.get();
            if (foundDrop.getUser().getId().equals(userId)) {
                return true;
            } else {
                return false;
            }
        }
    }


    public Boolean deleteDrop(Drop drop, User authUser) {
        Boolean isAuthorized = isDropBelongsToUser(drop, authUser);
        if (!isAuthorized) {
            throw new ForbiddenRequestException();
        } else {
            try {
                dropRepository.delete(drop);
                return true;
            }
            catch (Exception e) {
                return false;
            }
        }
    }


    @Override
    public List<Drop> queryDrops(String query, String username) {



        return null;
    }
}
