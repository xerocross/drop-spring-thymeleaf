package com.adamfgcross.drop.service;


import com.adamfgcross.drop.entity.Drop;

import java.util.List;

public interface QueryService {
    public List<Drop> queryDrops(String queryString, Long userId);
}
