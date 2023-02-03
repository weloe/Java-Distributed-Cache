package com.weloe.cache.cachemanager;

import java.time.LocalDateTime;

/**
 * @author weloe
 */
public class CacheObj {

    private Class clazz;

    private int byteSize;

    // 存储的实际数据
    private byte[] data;

    public CacheObj() {
    }

    public CacheObj(byte[] data){
        this.data = data;
        this.byteSize = data.length;
    }

    public CacheObj(LocalDateTime endTime,Class clazz ,int byteSize, byte[] data) {
        this.clazz = clazz;
        this.byteSize = byteSize;
        this.data = data;
    }

    public int getByteSize() {
        return byteSize;
    }

    public byte[] getData() {
        return data;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
