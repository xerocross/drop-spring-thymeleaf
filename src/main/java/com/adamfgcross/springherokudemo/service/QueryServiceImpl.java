package com.adamfgcross.springherokudemo.service;


import com.adamfgcross.springherokudemo.entity.Drop;
import com.adamfgcross.springherokudemo.querying.HashTagSearch;
import com.adamfgcross.springherokudemo.repository.DropRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Service
public class QueryServiceImpl implements QueryService {

    private final HashTagSearch hashTagSearch;
    private final DropRepository dropRepository;

    public QueryServiceImpl(DropRepository dropRepository, HashTagSearch hashTagSearch) {
        this.hashTagSearch = hashTagSearch;
        this.dropRepository = dropRepository;
    }

    public List<Drop> queryDrops(String queryString, Long userId) {
        Iterable<Drop> drops = dropRepository.findByUser_Id(userId);
        List<String> hashTags = hashTagSearch.getHashTags(queryString);
        return intersectHashTags(hashTags, drops);
    }

    private Boolean isDropMatchesHashTag(String hashTag, Drop drop) {
        String dropText = drop.getText();
        List<String> dropHashTags = hashTagSearch.getHashTags(dropText);
        return dropHashTags.contains(hashTag);
    }

    List<Drop> intersectHashTags(List<String> queryHashTags, Iterable<Drop> drops) {
        List<Drop> resultDrops = new ArrayList<>();
        Iterator<Drop> itr = drops.iterator();
        outer: while (itr.hasNext()) {
            Drop drop = itr.next();
            inner: for (String tag : queryHashTags) {
                if (!isDropMatchesHashTag(tag, drop)) {
                    continue outer;
                }
            }
            resultDrops.add(drop);
        }
        return resultDrops;
    }

}
