package com.weloe.cache.server.command.config;

import com.weloe.cache.cachemanager.Group;
import com.weloe.cache.cachemanager.GroupManager;

import java.util.List;

/**
 * config操作
 * @author weloe
 */
public class ConfigService {

    public Object getNormalSize(Group group){
        return group.getNormalSize();
    }

    public Object getMaxSize(Group group){
        return group.getMaxSize();
    }

    public Object setMaxSize(Group group,String size) {
        group.setMaxSize(Integer.parseInt(size));
        return "";
    }

    public Object setMaxNum(Group group,String num) {
        return null;
    }

}
