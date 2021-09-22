package com.adamfgcross.springherokudemo.repository;


import com.adamfgcross.springherokudemo.entity.Drop;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DropRepository extends CrudRepository<Drop, Long> {

    List<Drop> findByTextContains(String text);

    List<Drop> findByUser_Id(Long userId);

}
