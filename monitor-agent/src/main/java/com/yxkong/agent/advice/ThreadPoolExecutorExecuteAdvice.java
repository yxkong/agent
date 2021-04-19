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
 * @Date: 2021/4/11 5:34 下午
 * @version: 1.0
 */
public class ThreadPoolExecutorExecuteAdvice {
    /**
     * 对所有的线程的execute 进入方法进行监听
     * byteBuddy不支持对constructor
     * @Advice.OnMethodEnter 必须作用与static方法
     * @param obj
     * @param abc
     */
    @Advice.OnMethodEnter
    public static void executeBefore(@Advice.This Object obj,@Advice.Argument(0) Object abc){
       try{
           ThreadPoolExecutor executor = (ThreadPoolExecutor) obj;
           final ThreadFactory threadFactory = executor.getThreadFactory();
           if(threadFactory instanceof NamedThreadFactory){
               NamedThreadFactory namedThreadFactory = (NamedThreadFactory) threadFactory;
               ThreadPoolMonitorData.add(namedThreadFactory.getName(),namedThreadFactory.getDesc(),(ThreadPoolExecutor) obj);
           }else {
               ThreadPoolMonitorData.add(executor.hashCode()+"","未使用提供的NamedThreadFactory",(ThreadPoolExecutor) obj);
           }

       }catch (Exception e){
           e.printStackTrace();
       }
    }
    //@RuntimeType
    //public static Object execute(@Origin Method method,@AllArguments Object[] allArguments,
    //                           @SuperCall Callable<?> callable) throws Exception {
    //    long start = System.currentTimeMillis();
    //    try {
    //        for (Object argument : allArguments) {
    //            System.out.println(argument);
    //        }
    //        // 原有函数执行
    //        return callable.call();
    //    } finally {
    //        System.out.println("execute 花费时间:" + (System.currentTimeMillis() - start) + "ms");
    //    }
    //}

}