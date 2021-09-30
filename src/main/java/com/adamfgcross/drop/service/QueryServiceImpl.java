package com.adamfgcross.drop.service;


import com.adamfgcross.drop.entity.Drop;
import com.adamfgcross.drop.querying.HashTagSearch;
import com.adamfgcross.drop.repository.DropRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


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

        List<Drop> resultList;
        if (hashTags.size() > 0) {
            List<Drop> dropIntersection = intersectHashTags(hashTags, drops);
            resultList = dropIntersection;
        } else {
            // fall back on simpler logic
            List<Drop> dropsByWords = intersectDropsByWords(queryString, userId); //filterDropsByWords(queryString, userId);
            resultList = dropsByWords;
        }
        return resultList.stream().limit(7).collect(Collectors.toList());
    }

    private List<Drop> filterDropsByWords(String queryString,  Long userId) {
        List<String> words = getWords(queryString);
        //List<Drop> dropsByWords = new ArrayList<>();

        Set<Drop> dropsByWords = new HashSet<>();


        words.forEach(word-> {
            List<Drop> wordDrops = dropRepository.findByTextContainsAndUser_Id(word, userId);
            dropsByWords.addAll(wordDrops);
        });
        return new ArrayList<>(dropsByWords);
    }

    private List<Drop> intersectDropsByWords(String queryString, Long userId) {
        List<String> words = getWords(queryString);
        Iterable<Drop> drops = dropRepository.findByUser_Id(userId);
        List<Drop> resultDrops = new ArrayList<>();
        Iterator<Drop> itr = drops.iterator();
        outer: while (itr.hasNext()) {
            Drop drop = itr.next();
            inner: for (String word : words) {
                if (!isDropContainsWordIgnoreCase(word, drop)) {
                    continue outer;
                }
            }
            resultDrops.add(drop);
        }
        return resultDrops;
    }

    private Boolean isDropContainsWordIgnoreCase(String word, Drop drop) {
        return drop.getText().toUpperCase().contains(word.toUpperCase());
    }

    private Boolean isDropMatchesHashTag(String hashTag, Drop drop) {
        String dropText = drop.getText();
        List<String> dropHashTags = hashTagSearch.getHashTags(dropText);
        return dropHashTags.contains(hashTag);
    }


    private List<String> getWords (String queryString) {
        String[] words = queryString.split("\\s");
        List<String> wordList = new ArrayList<>(Arrays.asList(words));
        return wordList;
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
