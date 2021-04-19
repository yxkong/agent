package com.yxkong.agent.data;

import java.io.Serializable;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: yxkong
 * @Date: 2021/4/19 9:46 下午
 * @version: 1.0
 */
public class ThreadPoolExecutorWraper implements Serializable {

    private String name;
    private String desc;
    private ThreadPoolExecutor executor;

    public ThreadPoolExecutorWraper(String name, String desc, ThreadPoolExecutor executor) {
        this.name = name;
        this.desc = desc;
        this.executor = executor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    public void setExecutor(ThreadPoolExecutor executor) {
        this.executor = executor;
    }
}