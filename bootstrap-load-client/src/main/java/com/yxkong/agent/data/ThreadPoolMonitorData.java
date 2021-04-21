package com.yxkong.agent.data;

import com.yxkong.agent.NamedThreadFactory;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
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
     * @param executor
     */
    public  void add(ThreadPoolExecutor executor){
        final Pair<String, String> info = getInfo(executor);
        ThreadPoolExecutorWraper data = new ThreadPoolExecutorWraper(info.getKey(),info.getValue(),executor);
        put(info.getKey(),data);
    }
    private static Pair<String,String> getInfo(ThreadPoolExecutor executor){
        final ThreadFactory threadFactory = executor.getThreadFactory();
        String key = executor.getClass().getName()+"@"+executor.hashCode();
        String desc = "未使用提供的NamedThreadFactory";
        if(threadFactory instanceof NamedThreadFactory){
            NamedThreadFactory namedThreadFactory = (NamedThreadFactory) threadFactory;
            key = namedThreadFactory.getName();
            desc = namedThreadFactory.getDesc();
        }
        return new Pair<>(key,desc);
    }

    /**
     * 按照key 加锁
     * 按实际情况，线程池是稀缺资源，一个项目里不会有特别多
     * 只有放入了字符串常量池，字符串的对象才会唯一
     * @param name 线程池的名字
     * @param wraper  线程池包装类
     */
    private void put(String name,ThreadPoolExecutorWraper wraper){
        synchronized (name.intern()){
            if(!threadPoolExecutorWraperMap.containsKey(name)){
                threadPoolExecutorWraperMap.put(name,wraper);
            }
        }
    }

    /**
     * 对外暴露remove接口
     * @param executor
     */
    public static void remove(ThreadPoolExecutor executor){
        final Pair<String, String> info = getInfo(executor);
        if(threadPoolExecutorWraperMap.containsKey(info.getKey())){
            threadPoolExecutorWraperMap.remove(info.getKey());
        }
    }
}