package com.weloe.cache.util;

import java.lang.reflect.Method;

/**
 * 用于标记未完成代码
 */
public class ToDoUtil {

    public static void todo(Class clazz){
        throw new RuntimeException("待完成");
    }

}
