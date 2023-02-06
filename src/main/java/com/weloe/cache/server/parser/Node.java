package com.weloe.cache.server.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author weloe
 */
public class Node {
    /**
     * 待匹配的命令参数，只有最后一层才有 例如 set :group :key :value
     */
    private String pattern;

    /**
     * 命令参数的一部分例如 set :group :key :value 中的 :group
     */
    private String part;

    /**
     * 子节点
     */
    private List<Node> children;

    /**
     * 是否是通配符节点
     */
    private boolean isWild;

    public Node() {
        this.children = new LinkedList<>();
        this.part = "";
        this.pattern = "";
    }

    public Node(String part, boolean isWild) {
        this.part = part;
        this.isWild = isWild;
        this.children = new LinkedList<>();
    }

    /**
     * 根据part匹配子节点
     *
     * @param part
     * @return 第一个匹配节点
     */
    public Node matchChild(String part) {
        for (Node child : children) {
            if (child.part.equals(part) || child.isWild) {
                return child;
            }
        }
        return null;
    }

    /**
     * 根据part匹配子节点
     * @param part
     * @return 所有匹配的节点
     */
    public List<Node> matchChildren(String part) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node child : children) {
            if (child.part.equals(part) || child.isWild) {
                nodes.add(child);
            }
        }
        return nodes;
    }

    /**
     * 注册节点
     *
     * @param pattern
     * @param parts
     * @param height
     */
    public void insert(String pattern, String[] parts, int height) {
        // 终止条件,height匹配完，到了最下层
        if (parts.length == height) {
            this.pattern = pattern;
            return;
        }

        String part = parts[height];
        // 匹配出一个子节点
        Node child = matchChild(part);
        if (child == null) {
            // 如果当前part的第一个字符是":"或者"*"就为模糊匹配
            child = new Node(part, part.startsWith(":") || part.startsWith("*"));
            // 增加当前节点的子节点
            children.add(child);
        }
        child.insert(pattern, parts, height + 1);
    }

    /**
     * 根据parts[]匹配出节点
     * @param parts
     * @param height
     * @return
     */
    public Node search(String[] parts, int height) {
        // 匹配到末端
        if(parts.length == height || part.startsWith("*")){
            if(pattern == null){
                return null;
            }
            // 匹配到节点
            return this;
        }

        String part = parts[height];
        List<Node> children = matchChildren(part);

        for (Node child : children) {
            Node node = child.search(parts, height + 1);
            if(node != null){
                return node;
            }
        }

        return null;
    }


    public String getPattern() {
        return pattern;
    }
}
