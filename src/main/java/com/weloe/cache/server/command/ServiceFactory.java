package com.weloe.cache.server.command;

import com.weloe.cache.server.command.config.ConfigService;
import com.weloe.cache.server.command.delete.DeleteService;
import com.weloe.cache.server.command.expire.ExpireService;
import com.weloe.cache.server.command.group.GroupService;
import com.weloe.cache.server.command.str.StringService;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author weloe
 */
public class ServiceFactory {

    Map<String,Object> map;

    public ServiceFactory() {
        map = new LinkedHashMap<>();
        map.put("str",new StringService());
        map.put("group",new GroupService());
        map.put("config",new ConfigService());
        map.put("delete",new DeleteService());
        map.put("expire",new ExpireService());
    }


    public<T> T getBean(String name){
        return (T) map.get(name);
    }
}
