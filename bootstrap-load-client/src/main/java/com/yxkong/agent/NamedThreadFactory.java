package com.yxkong.agent;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 命名threadfactory，方便监控线程池
 *
 * @Author: yxkong
 * @Date: 2021/1/3 8:52 下午
 * @version: 1.0
 */
public class NamedThreadFactory implements ThreadFactory, Serializable {
    static final String DEFAULT_NAME = "execute";
    static final String DEFAULT_DESC = "default";
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final boolean daemon;
    private final String serviceName;
    private final String name;
    private final String namePrefix;
    private final String desc;
    public String getName(){
        return this.name;
    }
    public String getDesc(){
        return  this.desc;
    }
    public String getServiceName(){
        return  this.serviceName;
    }
    public NamedThreadFactory(Builder builder) {
        this.group = builder.group;
        this.daemon = builder.daemon;
        this.serviceName = builder.serviceName;
        this.name = builder.name;
        this.desc = builder.desc;
        this.namePrefix = builder.namePrefix;
    }

    public static class Builder{
        private  ThreadGroup group;
        private  boolean daemon = false;
        private  String serviceName;
        private  String name;
        private  String namePrefix;
        private  String desc;
        public NamedThreadFactory build(){
            SecurityManager s = System.getSecurityManager();
            this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            final int andIncrement = poolNumber.getAndIncrement();
            if(Objects.isNull(this.name)){
                this.name = DEFAULT_NAME;
            }
            this.namePrefix =String.format("%s-pool-%d-thread-",this.name,andIncrement);
            if(DEFAULT_NAME.equals(this.name)){
                this.name =String.format("%s-pool-%d",this.name,andIncrement);
            }
            if(Objects.isNull(this.desc)){
                this.desc = DEFAULT_DESC;
            }

            return new NamedThreadFactory(this);
        }
        public Builder daemon(boolean daemon){
            this.daemon = daemon;
            return this;
        }
        public Builder serviceName(String serviceName){
            this.serviceName = serviceName;
            return this;
        }
        public Builder name(String name){
            this.name = name;
            return this;
        }
        public Builder desc(String desc){
            this.desc = desc;
            return this;
        }


    }



    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon()){
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY){
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
    public static ThreadFactory defaultThreadFactory(boolean daemon) {
        return new Builder().daemon(daemon).build();
    }
}