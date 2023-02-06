package com.weloe.cache.server.command.str;

import com.weloe.cache.cachemanager.Group;
import com.weloe.cache.cachemanager.GroupManager;
import com.weloe.cache.server.command.group.GroupService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringServiceTest {
    private GroupManager groupManager = GroupManager.getInstance();
    GroupService groupService = new GroupService();
    @Test
    void get() {
        StringService stringService = new StringService();
        Object add = groupService.add("2");
        Group group = groupManager.getGroup("2");
        stringService.set(group,"2","2");
        Object s = stringService.get(group, "2");
        System.out.println(s);
    }

    @Test
    void set() {
    }
}