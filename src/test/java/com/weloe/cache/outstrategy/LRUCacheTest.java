package com.weloe.cache.outstrategy;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {

    @Test
    void lru(){
        CacheStrategy<Integer, Integer> lruCache = new LRUCache<>(5);
        lruCache.setCallback((integer, integer2) -> System.out.println("淘汰"+integer+"="+integer2));
        lruCache.put(1,1);
        lruCache.put(2,2);
        lruCache.put(3,3);
        lruCache.put(4,4);
        lruCache.put(5,5);
        lruCache.put(6,6);
        List list = lruCache.list();
        System.out.println(list);
    }

}