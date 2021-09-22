package com.adamfgcross.springherokudemo.service;


import com.adamfgcross.springherokudemo.entity.Drop;

import java.util.List;

public interface QueryService {
    public List<Drop> queryDrops(String queryString, Long userId);
}
