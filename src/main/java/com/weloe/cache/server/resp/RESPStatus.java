package com.weloe.cache.server.resp;

/**
 * @author weloe
 */
public enum RESPStatus {

    OK("+ok"),ERROR("-Error");

    private String msg;

    RESPStatus(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
