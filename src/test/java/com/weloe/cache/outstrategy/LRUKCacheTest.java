package com.weloe.cache.outstrategy;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LRUKCacheTest {

    @Test
    void lrukCacheTest() {
        LRUKCache<Integer, Integer> lrukCache = new LRUKCache<>(2,3,1);
        lrukCache.setHistoryListCallback((integer, integer2) -> System.out.println("记录队列淘汰"+integer+"="+integer2));
        lrukCache.setCallback((integer, integer2) -> System.out.println("缓存淘汰"+integer+"="+integer2));
        lrukCache.get(1);
        lrukCache.get(1);
        lrukCache.get(1);
        lrukCache.get(2);
        lrukCache.get(2);
        lrukCache.get(2);
        lrukCache.get(3);
        lrukCache.get(3);
        lrukCache.get(3);
        lrukCache.get(4);
        lrukCache.get(4);
        lrukCache.get(4);
        lrukCache.put(1,2);
        lrukCache.put(2,2);
        lrukCache.put(3,2);
        lrukCache.put(4,2);
        List list = lrukCache.list();
        System.out.println(list);
    }
}