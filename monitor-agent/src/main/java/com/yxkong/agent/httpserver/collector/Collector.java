package com.yxkong.agent.httpserver.collector;


import com.yxkong.agent.dto.ResultBean;

import java.util.Map;


/**
 * 自定义收集器模板
 */
public abstract class Collector {
    /**
     * 方法名称,也是接口名称
     */
    protected String methodName;
    public String getMethodName(){
        return this.methodName;
    }

    /**
     * 每个收集器去实现自己的收集功能
     * @return
     */
    public abstract ResultBean collect(Map<String,String> params);
    /**
     * Register the Collector with the default registry.
     */
    public <T extends Collector> T register() {
        return register(CollectorRegistry.defaultRegistry);
    }

    /**
     * Register the Collector with the given registry.
     */
    public <T extends Collector> T register(CollectorRegistry registry) {
        registry.register(this);
        return (T)this;
    }

}
