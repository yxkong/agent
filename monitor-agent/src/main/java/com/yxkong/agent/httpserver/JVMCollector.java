package com.yxkong.agent.httpserver;

import com.yxkong.agent.dto.ResultBean;
import com.yxkong.agent.httpserver.collector.Collector;
import com.yxkong.agent.utils.JVMGCInfo;
import com.yxkong.agent.utils.JVMInfo;
import com.yxkong.agent.utils.JVMMemoryInfo;
import com.yxkong.agent.utils.JVMThreadInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * jvm监控是现实
 * 参考：https://blog.csdn.net/tjiyu/article/details/103888169
 * @Author: yxkong
 * @Date: 2021/4/19 12:38 下午
 * @version: 1.0
 */
public class JVMCollector extends Collector {
    public JVMCollector() {
        this.methodName = "jvm";
    }

    @Override
    public ResultBean collect(Map<String,String> params) {
        Map<String,Object> map = new HashMap<>();
        map.put("GC", JVMGCInfo.getInfo());
        map.put("JVM", JVMInfo.getInfo());
        map.put("memory", JVMMemoryInfo.getInfo());
        map.put("thread", JVMThreadInfo.getInfo());

        return new ResultBean.Builder().success(map).build();
    }
}