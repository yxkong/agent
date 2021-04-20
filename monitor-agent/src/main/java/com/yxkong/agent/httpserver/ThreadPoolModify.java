package com.yxkong.agent.httpserver;

import com.yxkong.agent.data.ThreadPoolExecutorWraper;
import com.yxkong.agent.data.ThreadPoolMonitorData;
import com.yxkong.agent.dto.ResultBean;
import com.yxkong.agent.httpserver.collector.Collector;
import com.yxkong.agent.utils.StringUtils;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池信息修改
 * @author yxkong
 * @version 1.0
 * @date 2021/4/18 19:44
 */
public class ThreadPoolModify extends Collector {
    public ThreadPoolModify(){
        this.methodName = "threadPool/modify";
    }
    @Override
    public ResultBean collect(Map<String,String> params) {

        try {
            Map<String, ThreadPoolExecutorWraper> alls = ThreadPoolMonitorData.alls();
            String key = params.get("key");
            String coreSize = params.getOrDefault("coreSize",null);
            String maximumPoolSize = params.getOrDefault("maximumPoolSize",null);
            ThreadPoolExecutorWraper executorWraper = null;
            if(StringUtils.isNotEmpty(key)){
                executorWraper = alls.get(key);
            }
            if(null == executorWraper){
                return new ResultBean.Builder().status("1000").message("没有找到对应的线程池").build();
            }
            ThreadPoolExecutor executor = executorWraper.getExecutor();
            if(StringUtils.isNotEmpty(coreSize)){
                executor.setCorePoolSize(Integer.parseInt(coreSize));
            }
            if(StringUtils.isNotEmpty(maximumPoolSize)){
                executor.setMaximumPoolSize(Integer.parseInt(maximumPoolSize));
            }
            //启动所有的核心线程数，getTask中不会根据核心线程数修改workers，如果再有新线程，会动态调整
            executor.prestartAllCoreThreads();
            //如果KeepAliveTime为0，不能修改
            if(executor.getKeepAliveTime(TimeUnit.MILLISECONDS)>0){
                //如果将线程池改小，设置下，默认核心线程数是不会回收的
                executor.allowCoreThreadTimeOut(true);
            }
            BlockingQueue<Runnable> queue = executor.getQueue();
            /**
             * 通过反射修改队列的长度 TODO
             */
            return new ResultBean.Builder().success(executor).build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResultBean.Builder().fail(null).build();
    }
}
