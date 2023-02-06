package com.weloe.cache.server.command.group;

import com.weloe.cache.cachemanager.Cache;
import com.weloe.cache.cachemanager.Group;
import com.weloe.cache.cachemanager.GroupManager;

import java.util.List;

/**
 * group操作
 * @author weloe
 */
public class GroupService {

    private GroupManager groupManager = GroupManager.getInstance();

    public Object add(String groupName) {

        Group group = new Group();
        group.setName(groupName);
        group.setCache(new Cache());
        group.setGetter(k -> null);
        groupManager.put(group);

        return "";
    }


}
