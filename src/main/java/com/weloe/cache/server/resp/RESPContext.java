package com.weloe.cache.server.resp;

import com.weloe.cache.cachemanager.Group;
import com.weloe.cache.server.parser.Router;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @author weloe
 */
public class RESPContext {

    private LocalDateTime startTime;

    private Socket socket;

    private RESPRequest request;

    private RESPResponse response;

    private Router.Route route;

    private Map<String,String> paramMap;

    private Group group;


    public void initServer(Socket socket, RESPRequest request, RESPResponse response) throws IOException {

        this.socket = socket;
        this.request = request;
        this.response = response;
        this.startTime = LocalDateTime.now();
    }


    /**
     * 解析RESP协议的字节
     *
     * @return
     */
    public String parseRESPBytes() {
        String result = request.parseRESPBytes();

        return result;
    }

    /**
     * 解析RESP协议的String
     *
     * @param raw
     * @return
     */
    public Object parseRESPString(String raw) {
        Object obj = request.parseRESPString(raw);
        return obj;
    }

    public void ok() {
        response.ok();
    }

    public void ok(byte[] bytes){
        response.ok(String.valueOf(bytes));
    }

    public void ok(String arg) {
        response.ok(arg);
    }

    public void ok(String... args) {
        response.ok(args);
    }

    public void error(String msg) {
        response.error(msg);
    }

    public void close() {
        if (request != null) {
            try {
                request.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (response != null) {
            response.close();
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public Socket getSocket() {
        return socket;
    }

    public RESPRequest getRequest() {
        return request;
    }

    public RESPResponse getResponse() {
        return response;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setRoute(Router.Route route) {
        this.route = route;
    }

    public Router.Route getRoute() {
        return route;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public String getParam(String key){
        return paramMap.get(key);
    }
}
