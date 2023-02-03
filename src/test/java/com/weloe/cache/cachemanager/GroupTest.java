package com.weloe.cache.cachemanager;

import com.weloe.cache.outstrategy.CacheStrategy;
import com.weloe.cache.outstrategy.LRUCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

class GroupTest {
    Group group;

    @BeforeEach
    void setUp() {

        CacheStrategy<String, CacheObj> lruCache = new LRUCache<>(5);
        lruCache.setCallback((s1, s2)-> System.out.println("缓存淘汰"));

        group = new Group("group1", new Cache(1024*1024,lruCache), str -> {
            System.out.println("group1回调");
            return new byte[0];
        });


    }

    @Test
    void get() {
        CacheObj cacheObj = group.get("1");
    }

    @Test
    void getName() {
        String name = group.getName();
        System.out.println(name);
    }

    @Test
    void putCacheObj() {
        String x = "132";
        group.putCacheObj("cache1",new CacheObj(null,String.class,x.getBytes(StandardCharsets.UTF_8).length,x.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void expire() {
        String x = "132";
        group.putCacheObj("cache1",new CacheObj(null,String.class,x.getBytes(StandardCharsets.UTF_8).length,x.getBytes(StandardCharsets.UTF_8)));
        group.expire("cache1", 2, ChronoUnit.MINUTES);

        System.out.println(group.ttl("cache1"));
    }
}