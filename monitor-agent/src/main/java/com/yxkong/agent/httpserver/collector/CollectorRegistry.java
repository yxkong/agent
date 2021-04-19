package com.yxkong.agent.httpserver.collector;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 收集器注册
 */
public class CollectorRegistry {
    /**
     * The default registry.
     */
    public static final CollectorRegistry defaultRegistry = new CollectorRegistry();

    /**
     * 声明一个jvm全局锁
     */
    private static final Object namesCollectorsLock = new Object();
    /**
     * 存放menthodName 和collector的映射
     */
    private final Map<String, Collector> namesToCollectors = new HashMap<String, Collector>();


    public CollectorRegistry() {
    }


    /**
     * 注册一个Collector
     */
    public void register(Collector m) {
        String name =  m.getMethodName();
        synchronized (namesCollectorsLock) {
            if (namesToCollectors.containsKey(name)) {
                throw new IllegalArgumentException("Collector already registered that provides name: " + name);
            }
            namesToCollectors.put(name, m);
        }
    }

    /**
     * 卸载一个Collector.
     */
    public void unregister(Collector m) {
        synchronized (namesCollectorsLock) {
            namesToCollectors.remove(m.getMethodName());
        }
    }

    /**
     * 卸载所有的 Collectors.
     */
    public void clear() {
        synchronized (namesCollectorsLock) {
            namesToCollectors.clear();
        }
    }

    /**
     * A snapshot of the current collectors.
     */
    private Set<Collector> collectors() {
        synchronized (namesCollectorsLock) {
            return new HashSet<Collector>(namesToCollectors.values());
        }
    }

    /**
     * 获取Collector 并执行对应的collect()
     * @param name
     * @param params
     * @return
     */
    public String filteredCollector(String name,Map<String,String> params ) {
        Collector collector = namesToCollectors.get(name);
        return JSON.toJSON(collector.collect(params)).toString();
    }

}