package com.yxkong.agent.httpserver;

import com.yxkong.agent.data.ThreadPoolExecutorWraper;
import com.yxkong.agent.data.ThreadPoolMonitorData;
import com.yxkong.agent.dto.ResultBean;
import com.yxkong.agent.dto.ThreadPoolVo;
import com.yxkong.agent.httpserver.collector.Collector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

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
            ThreadPoolVo vo =  new ThreadPoolVo.Builder()
                    .name(executor.getName())
                    .desc(executor.getDesc())
                    .threadPoolExecutor(executor.getExecutor())
                    .build();
            return new ResultBean.Builder().success(vo).build();
        }
        Map<String,ThreadPoolVo> map = new HashMap<>();
        alls.forEach((k,v)->{
            ThreadPoolVo threadPoolVo =  new ThreadPoolVo.Builder()
                    .name(v.getName())
                    .desc(v.getDesc())
                    .threadPoolExecutor(v.getExecutor())
                    .build();
            map.put(k,threadPoolVo);
        });
        return new ResultBean.Builder().success(map).build();
    }
}
