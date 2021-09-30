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
    public Boolean save (Drop drop) {
        return drop.equals(dropRepository.save(drop));
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
}
