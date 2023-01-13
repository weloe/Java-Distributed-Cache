package com.weloe.cache.cachemanager;

import com.weloe.cache.outstrategy.LRUCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

class GroupManagerTest {
    GroupManager groupManager;

    @BeforeEach
    void setUp() {
        Group group1 = new Group("group1",
                new Cache(1024*1024, new LRUCache<>(5,(s1, s2)-> System.out.println("group1缓存淘汰"))),
                str -> {System.out.println("group1未获取缓存的回调");return new byte[0];}
        );
        Group group2 = new Group("group2",
                new Cache(1024*1024, new LRUCache<>(5,(s1, s2)-> System.out.println("group2缓存淘汰"))),
                str -> {System.out.println("group2未获取缓存的回调");return new byte[0];}
        );

        groupManager = new GroupManager(new HashMap<>(),new ReentrantLock());
        groupManager.put(group1);
        groupManager.put(group2);


    }

    @Test
    void getGroup() {

        System.out.println(groupManager.getGroup(""));
        System.out.println(groupManager.getGroup("group1"));
        System.out.println(groupManager.getGroup("group2").getName());

    }

    @Test
    void put() {
        Group group3 = new Group("group3",
                new Cache(1024*1024, new LRUCache<>(5,(s1, s2)-> System.out.println("group3缓存淘汰"))),
                str -> {System.out.println("group3未获取缓存的回调");return new byte[0];}
        );
        groupManager.put(group3);
        System.out.println(groupManager.getGroup("group3").getName());
    }
}