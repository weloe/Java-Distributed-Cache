package com.weloe.cache.cachemanager;

import com.weloe.cache.server.parser.CommandParser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author weloe
 */
public class GroupManager {

    private static GroupManager instance = new GroupManager(new HashMap<>(),new ReentrantLock());

    private GroupManager (){}

    public static GroupManager getInstance() {
        return instance;
    }

    private Map<String, Group> groupMap;

    //Lock readLock;

    public GroupManager(Map<String, Group> groupMap, Lock lock) {
        this.groupMap = groupMap;
        //this.readLock = new ReentrantReadWriteLock().readLock();
    }


    public Group getGroup(String key) {
        //readLock.lock();

        Group group = groupMap.get(key);
        //readLock.unlock();

        return group;
    }

    public Group put(Group group){
        return groupMap.put(group.getName(),group);
    }

    public CacheObj get(String group,String key){
        return getGroup(group).get(key);
    }
}
