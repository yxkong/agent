package com.yxkong.agent.data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池监控data
 * @Author: yxk
 * @Date: 2021/4/3 10:29 上午
 * @version: 1.0
 */
public class ThreadPoolMonitorData {
    private static Map<String, ThreadPoolExecutorWraper> threadPoolExecutorWraperMap = new HashMap<>();

    public static Map<String, ThreadPoolExecutorWraper> alls(){
        return threadPoolExecutorWraperMap;
    }
    /**
     * 对外暴露添加接口
     * @param name 线程池名称
     * @param desc 线程池描述
     * @param threadPoolExecutor
     */
    public static void add(String name,String desc,ThreadPoolExecutor threadPoolExecutor){
        ThreadPoolExecutorWraper data = new ThreadPoolExecutorWraper(name,desc,threadPoolExecutor);
        if(!threadPoolExecutorWraperMap.containsKey(name)){
            threadPoolExecutorWraperMap.put(name,data);
        }
    }

    /**
     * 对外暴露remove接口
     * @param name
     */
    public static void remove(String name){
        if(threadPoolExecutorWraperMap.containsKey(name)){
            threadPoolExecutorWraperMap.remove(name);
        }
    }
}