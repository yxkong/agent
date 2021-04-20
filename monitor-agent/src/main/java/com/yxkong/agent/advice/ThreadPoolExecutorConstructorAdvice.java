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
                //以下代码不能抽取，一旦抽取，必须用bootstrap加载器加载
                ThreadPoolExecutor executor = (ThreadPoolExecutor)obj;
                final ThreadFactory threadFactory = executor.getThreadFactory();
                if(threadFactory instanceof NamedThreadFactory){
                    NamedThreadFactory namedThreadFactory = (NamedThreadFactory) threadFactory;
                    ThreadPoolMonitorData.add(namedThreadFactory.getName(),namedThreadFactory.getDesc(), executor);
                }else {
                    ThreadPoolMonitorData.add(executor.getClass().getName()+"@"+executor.hashCode(),"未使用提供的NamedThreadFactory", executor);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}