package com.yxkong.agent.dto;

import java.io.Serializable;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * vo
 *
 * @Author: yxkong
 * @Date: 2021/4/20 4:31 下午
 * @version: 1.0
 */
public class ThreadPoolVo implements Serializable {
    private String name;
    private String desc;
    private int activeCount;
    private int largestPoolSize;
    private int poolSize;
    private long taskCount;
    private int corePoolSize;
    private long completedTaskCount;
    private Boolean terminating;
    private int maximumPoolSize;
    private int queueSize;
    private Boolean shutdown;
    private Boolean terminated;

    public ThreadPoolVo(Builder builder) {
        this.name = builder.name;
        this.desc = builder.desc;
        this.activeCount = builder.activeCount;
        this.largestPoolSize = builder.largestPoolSize;
        this.poolSize = builder.poolSize;
        this.taskCount = builder.taskCount;
        this.corePoolSize = builder.corePoolSize;
        this.completedTaskCount = builder.completedTaskCount;
        this.terminating = builder.terminating;
        this.maximumPoolSize = builder.maximumPoolSize;
        this.queueSize = builder.queueSize;
        this.shutdown = builder.shutdown;
        this.terminated = builder.terminated;


    }

    public static class Builder{
        private String name;
        private String desc;
        private int activeCount;
        private int largestPoolSize;
        private int poolSize;
        private long taskCount;
        private int corePoolSize;
        private long completedTaskCount;
        private Boolean terminating;
        private int maximumPoolSize;
        private int queueSize;
        private Boolean shutdown;
        private Boolean terminated;
        public ThreadPoolVo build(){
            return new ThreadPoolVo(this);
        }
        public Builder name(String name){
            this.name = name;
            return this;
        }
        public Builder desc(String desc){
            this.desc = desc;
            return this;
        }
        public Builder threadPoolExecutor(ThreadPoolExecutor executor){
            this.activeCount = executor.getActiveCount();
            this.largestPoolSize = executor.getLargestPoolSize();
            this.poolSize = executor.getPoolSize();
            this.taskCount = executor.getTaskCount();
            this.corePoolSize = executor.getCorePoolSize();
            this.completedTaskCount = executor.getCompletedTaskCount();
            this.terminating = executor.isTerminating();
            this.maximumPoolSize = executor.getMaximumPoolSize();
            this.queueSize = executor.getQueue().size();
            this.shutdown = executor.isShutdown();
            this.terminated = executor.isTerminated();
            return this;
        }
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getActiveCount() {
        return activeCount;
    }

    public int getLargestPoolSize() {
        return largestPoolSize;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public long getTaskCount() {
        return taskCount;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public Boolean getTerminating() {
        return terminating;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public Boolean getShutdown() {
        return shutdown;
    }

    public Boolean getTerminated() {
        return terminated;
    }
}