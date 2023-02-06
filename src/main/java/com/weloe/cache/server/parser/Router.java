package com.weloe.cache.server.parser;

import com.weloe.cache.server.resp.RESPContext;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author weloe
 */
public class Router {

    @FunctionalInterface
    public interface HandlerFunc {
        Object handle(RESPContext context);
    }

    public class Route{
        private Node node;
        private Map<String,String> map;

        public Route(Node node, Map<String, String> map) {
            this.node = node;
            this.map = map;
        }

        public Object handle(RESPContext context){
            context.setParamMap(map);
            String key = "command"+"-"+node.getPattern();
            return handlers.get(key).handle(context);
        }

        public Map<String, String> getMap() {
            return map;
        }

        public Node getNode() {
            return node;
        }
    }

    /**
     * 根节点
     */
    private Map<String,Node> roots;

    private Map<String,HandlerFunc> handlers;


    public Router() {
        this.roots = new LinkedHashMap<>();
        this.handlers = new LinkedHashMap<>();
    }

    /**
     * 解析pattern
     * @param pattern
     * @return
     */
    public String[] parsePattern(String pattern){
        String[] patterns = pattern.split(" ");

        String[] parts = new String[patterns.length];
        for (int i = 0; i < patterns.length; i++) {
            parts[i] = patterns[i];
            if(patterns[i].charAt(0) == '*'){
                break;
            }
        }

        return parts;
    }

    public Router addRoute(String pattern,HandlerFunc handler){
        addRoute("command",pattern,handler);
        return this;
    }

    public void addRoute(String method,String pattern,HandlerFunc handler){
        String[] parts = parsePattern(pattern);

        String key = method + "-" + pattern;
        Node node = roots.get(method);
        if (node == null) {
            roots.put(method,new Node());
        }
        roots.get(method).insert(pattern,parts,0);
        handlers.put(key,handler);
    }

    public Route getRoute(String path){
        return getRoute("command",path);
    }

    public Route getRoute(String method,String path){

        String[] patterns = parsePattern(path);
        Map<String, String> params = new LinkedHashMap<>();

        Node root = roots.get(method);
        if(root == null){
            return null;
        }
        Node res = root.search(patterns, 0);

        if (res == null) {
            return null;
        }
        String[] parts = parsePattern(res.getPattern());
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.charAt(0) == ':') {
                params.put(part.substring(1),patterns[i]);
            }
            if(part.charAt(0) == '*' && part.length() > 1){
                String collect = Arrays.stream(patterns).skip(i).collect(Collectors.joining(" "));
                params.put(part.substring(1),collect);
                break;
            }

        }

        return new Route(res,params);
    }



    public Map<String, HandlerFunc> getHandlers() {
        return handlers;
    }
}
