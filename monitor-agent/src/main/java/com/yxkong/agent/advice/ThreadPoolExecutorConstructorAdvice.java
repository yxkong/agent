package com.yxkong.agent.advice;

import com.yxkong.agent.NamedThreadFactory;
import com.yxkong.agent.data.ThreadPoolMonitorData;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 针对构造函数进行增强
 * @Author: yxkong
 * @Date: 2021/4/20 11:37 上午
 * @version: 1.0
 */
public class ThreadPoolExecutorConstructorAdvice {
    @Advice.OnMethodExit
    public static void constructor(@Advice.This Object obj, @Advice.AllArguments Object[] args){
        try {
            if(null!= args && args.length==7){
                ThreadPoolExecutor executor = (ThreadPoolExecutor)obj;
                ThreadPoolMonitorData data = new ThreadPoolMonitorData();
                data.add(executor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}