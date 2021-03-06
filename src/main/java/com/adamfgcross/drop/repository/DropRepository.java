package com.adamfgcross.drop.repository;


import com.adamfgcross.drop.entity.Drop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DropRepository extends CrudRepository<Drop, Long> {

    List<Drop> findByTextContains(String text);

    List<Drop> findByUser_Id(Long userId);

    List<Drop> findByTextContainsAndUser_Id(String text, Long userId);

}
