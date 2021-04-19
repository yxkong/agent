package com.yxkong.agent.httpserver;

import com.yxkong.agent.dto.ResultBean;
import com.yxkong.agent.httpserver.collector.Collector;

import java.util.Map;


/**
 *
 * @author yxkong
 * @version 1.0
 * @date 2021/3/19 12:32
 */
public class StackCollector extends Collector {

    public  StackCollector(){
        this.methodName = "stack";
    }

    @Override
    public ResultBean collect(Map<String,String> params) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        return new ResultBean.Builder().success(elements).build();
    }
}
