package com.yxkong.agent.httpserver;

import com.yxkong.agent.data.ThreadPoolExecutorWraper;
import com.yxkong.agent.data.ThreadPoolMonitorData;
import com.yxkong.agent.dto.ResultBean;
import com.yxkong.agent.httpserver.collector.Collector;

import java.util.Map;

/**
 * 线程池信息收集
 *
 * @author yxkong
 * @version 1.0
 * @date 2021/4/18 19:44
 */
public class ThreadPoolCollector extends Collector {
    public ThreadPoolCollector(){
        this.methodName = "threadPool";
    }
    @Override
    public ResultBean collect(Map<String,String> params) {
        Map<String, ThreadPoolExecutorWraper> alls = ThreadPoolMonitorData.alls();
        String key = params.get("key");
        ThreadPoolExecutorWraper executor = alls.get(key);
        if(null != executor){
            return new ResultBean.Builder().success(executor).build();
        }
        return new ResultBean.Builder().success(alls).build();
    }
}
