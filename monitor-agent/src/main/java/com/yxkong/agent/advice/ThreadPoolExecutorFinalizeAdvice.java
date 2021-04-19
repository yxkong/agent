package com.yxkong.agent.advice;

import com.yxkong.agent.NamedThreadFactory;
import com.yxkong.agent.data.ThreadPoolMonitorData;
import net.bytebuddy.asm.Advice;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池advice
 *
 * @Author: yxk
 * @Date: 2021/4/2 5:34 下午
 * @version: 1.0
 */
public class ThreadPoolExecutorFinalizeAdvice {
    @Advice.OnMethodEnter
    public static void finalize(@Advice.This Object obj){
        ThreadPoolExecutor executor = (ThreadPoolExecutor) obj;
        final ThreadFactory threadFactory = executor.getThreadFactory();
        if(threadFactory instanceof NamedThreadFactory){
            NamedThreadFactory namedThreadFactory = (NamedThreadFactory) threadFactory;
            ThreadPoolMonitorData.remove(namedThreadFactory.getName());
        }else {
            ThreadPoolMonitorData.remove(obj.hashCode()+"");
        }
    }

}