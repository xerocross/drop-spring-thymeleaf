package com.adamfgcross.springherokudemo.service;


import com.adamfgcross.springherokudemo.entity.Drop;
import com.adamfgcross.springherokudemo.querying.HashTagSearch;
import com.adamfgcross.springherokudemo.repository.DropRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QueryServiceImplTest {

    private QueryServiceImpl queryService;
    private Drop drop1;
    private Drop drop2;
    private Drop drop3;
    DropRepository dropRepository = mock(DropRepository.class);

    @BeforeEach
    void beforeAll() {
        drop1 = new Drop();
        drop1.setId(1L);
        drop2 = new Drop();
        drop2.setId(2L);
        drop3 = new Drop();
        drop3.setId(3L);
    }

    @Test
    void canCreateClass() {
        queryService = new QueryServiceImpl(dropRepository, new HashTagSearch());
    }

    @Test
    void canSearchOneTag() {
        HashTagSearch hashTagSearch = mock(HashTagSearch.class);
        when(hashTagSearch.getHashTags(anyString())).thenReturn(Arrays.asList("apple"));
        List<Drop> drops = Arrays.asList(drop1, drop2);
        drop1.setText("has #apple");
        drop2.setText("has #pear");
        List<String> hashTags = Arrays.asList("apple");
        queryService = new QueryServiceImpl(dropRepository, hashTagSearch);
        assert(queryService.intersectHashTags(hashTags, drops)).contains(drop1);
    }


    @Test
    void doesNotFindNonexistentTag() {
        drop1.setText("has #apple");
        drop2.setText("has #pear");
        drop3.setText("has #apple and #pear");
        List<Drop> drops = Arrays.asList(drop1, drop2, drop3);
        List<String> hashTags = Arrays.asList("apple", "pear");
        queryService = new QueryServiceImpl(dropRepository, new HashTagSearch());
        assert(queryService.intersectHashTags(hashTags, drops)).contains(drop3);
    }

    @Test
    void findsIntersectionDrop() {
        drop1.setText("has #apple");
        drop2.setText("has #pear");
        drop3.setText("has #apple and #pear");
        List<Drop> drops = Arrays.asList(drop1, drop2, drop3);
        List<String> hashTags = Arrays.asList("apple", "pear");
        queryService = new QueryServiceImpl(dropRepository, new HashTagSearch());
        assert(queryService.intersectHashTags(hashTags, drops)).contains(drop3);
    }

    @Test
    void intersectionExcludesDropsNotMatchingAllTags() {
        drop1.setText("has #apple");
        drop2.setText("has #pear");
        drop3.setText("has #apple and #pear");
        List<Drop> drops = Arrays.asList(drop1, drop2, drop3);
        List<String> hashTags = Arrays.asList("apple", "pear");
        queryService = new QueryServiceImpl(dropRepository, new HashTagSearch());
        List<Drop> resultDrops = queryService.intersectHashTags(hashTags, drops);
        System.out.println(resultDrops);
        assertThat(queryService.intersectHashTags(hashTags, drops), not(hasItem(drop1)));
    }

    @Test
    void withoutHashTagFallsBackToSearchByWord_basicPositive() {
        drop1.setText("has apple");
        drop2.setText("has pear");
        drop3.setText("has apple and pear");
        queryService = new QueryServiceImpl(dropRepository, new HashTagSearch());
        Long fakeUserId = 5L;
        String queryString = "apple";
        when(dropRepository.findByUser_Id(anyLong())).thenReturn(Arrays.asList(drop1, drop2, drop3));
        List<Drop> resultDrops = queryService.queryDrops(queryString, fakeUserId);
        assert(resultDrops).contains(drop3);

    }

    @Test
    void withoutHashTagFallsBackToSearchByWord_basicNegative() {
        drop1.setText("has apple");
        drop2.setText("has pear");
        drop3.setText("has apple and pear");
        queryService = new QueryServiceImpl(dropRepository, new HashTagSearch());
        Long fakeUserId = 5L;
        String queryString = "apple";
        when(dropRepository.findByUser_Id(anyLong())).thenReturn(Arrays.asList(drop1, drop2, drop3));
        List<Drop> resultDrops = queryService.queryDrops(queryString, fakeUserId);
        assertThat(resultDrops, not(hasItem(drop2)));
    }

    @Test
    void withoutHashTagFallsBackToSearchByWord_twoWord() {
        drop1.setText("has apple");
        drop2.setText("has pear");
        drop3.setText("has apple and pear");
        queryService = new QueryServiceImpl(dropRepository, new HashTagSearch());
        Long fakeUserId = 5L;
        String queryString = "apple pear";
        when(dropRepository.findByUser_Id(anyLong())).thenReturn(Arrays.asList(drop1, drop2, drop3));
        List<Drop> resultDrops = queryService.queryDrops(queryString, fakeUserId);
        assertThat(resultDrops, hasItem(drop3));
    }

    @Test
    void withoutHashTagFallsBackToSearchByWord_twoWordNotContainsJustOne() {
        drop1.setText("has apple");
        drop2.setText("has pear");
        drop3.setText("has apple and pear");
        queryService = new QueryServiceImpl(dropRepository, new HashTagSearch());
        Long fakeUserId = 5L;
        String queryString = "apple pear";
        when(dropRepository.findByUser_Id(anyLong())).thenReturn(Arrays.asList(drop1, drop2, drop3));
        List<Drop> resultDrops = queryService.queryDrops(queryString, fakeUserId);
        assertThat(resultDrops, not(hasItem(drop1)));
    }
}
