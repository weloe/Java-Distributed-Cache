package com.weloe.cache.server.command.expire;

import com.weloe.cache.cachemanager.Group;
import com.weloe.cache.cachemanager.GroupManager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * 设置,获取key的过期时间，单位秒
 * @author weloe
 */
public class ExpireService {

    public Object expire(Group group,String key,String value) {
        LocalDateTime time = group.expire(key, Long.parseLong(value), ChronoUnit.SECONDS);
        return time;
    }

    public Object ttl(Group group,String key) {
        long ttl = group.ttl(key);
        return ttl;
    }
}
