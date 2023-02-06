package com.weloe.cache.server.command.delete;

import com.weloe.cache.cachemanager.CacheObj;
import com.weloe.cache.cachemanager.Group;
import com.weloe.cache.cachemanager.GroupManager;

import java.util.List;

/**
 * 删除key相关操作
 * @author weloe
 */
public class DeleteService {

    public Object delete(Group group,String key) {
        CacheObj delete = group.delete(key);
        if(delete == null){
            return null;
        }
        return "";
    }


    public Object clear(Group group) {
        group.clear();
        return "";
    }
}
