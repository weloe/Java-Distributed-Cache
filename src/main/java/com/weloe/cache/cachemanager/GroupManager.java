package com.weloe.cache.cachemanager;

import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * @author weloe
 */
public class GroupManager {

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


}
