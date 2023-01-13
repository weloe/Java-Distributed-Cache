package com.weloe.cache.cachemanager;

import java.time.LocalDateTime;

/**
 * @author weloe
 */
public class CacheObj {

    private LocalDateTime endTime;

    private Class clazz;

    private int byteSize;

    // 存储的实际数据
    private byte[] data;

    public CacheObj() {
    }

    public CacheObj(LocalDateTime endTime,Class clazz ,int byteSize, byte[] data) {
        this.endTime = endTime;
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


    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
