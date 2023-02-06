package com.weloe.cache.server.command.str;

import com.weloe.cache.cachemanager.CacheObj;
import com.weloe.cache.cachemanager.Group;

import java.nio.charset.StandardCharsets;

/**
 * String操作
 * @author weloe
 */
public class StringService {

    public String get(Group group,String key) {
        CacheObj cacheObj = group.get(key);
        if(cacheObj != null){
            return new String(cacheObj.getData());
        }
        return null;
    }


    public Object set(Group group,String key,String value) {
        group.putCacheObj(key, new CacheObj(value.getBytes(StandardCharsets.UTF_8)));
        return "";
    }
}
