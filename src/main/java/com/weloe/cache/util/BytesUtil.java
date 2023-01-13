package com.weloe.cache.util;

import com.weloe.cache.cachemanager.CacheObj;

/**
 * 字节数组工具类
 * @author weloe
 */
public class BytesUtil {
    /**
     * 把 byte[] 转成 CacheObj.class
     * @param bytes
     * @return
     */
    public static CacheObj bytes2CacheObj(byte[] bytes){
        // TODO: 2022/12/10
        ToDoUtil.todo(BytesUtil.class);
        CacheObj cacheObj = new CacheObj();
        return cacheObj;
    }

    /**
     * 把 byte[] 转成 String
     * @param bytes
     * @return
     */
    public static String bytes2String(byte[] bytes){
        return new String(bytes);
    }

}
