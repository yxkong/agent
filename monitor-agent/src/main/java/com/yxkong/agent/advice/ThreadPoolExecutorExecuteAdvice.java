package com.yxkong.agent.advice;

import com.yxkong.agent.NamedThreadFactory;
import com.yxkong.agent.data.ThreadPoolMonitorData;
import net.bytebuddy.asm.Advice;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * 线程池Execute增强
 *
 * @Author: yxk
 * @Date: 2021/4/11 5:34 下午
 * @version: 1.0
 */
public class ThreadPoolExecutorExecuteAdvice {
    /**
     * 对所有的线程的execute 进入方法进行增强
     * byteBuddy不支持对constructor
     * @Advice.OnMethodEnter 必须作用与static方法
     * @param obj
     * @param abc
     */
    @Advice.OnMethodEnter
    public static void executeBefore(@Advice.This Object obj,@Advice.Argument(0) Object abc){
       try{
           //以下代码不能抽取，一旦抽取，必须用bootstrap加载器加载
           ThreadPoolExecutor executor = (ThreadPoolExecutor)obj;
           final ThreadFactory threadFactory = executor.getThreadFactory();
           if(threadFactory instanceof NamedThreadFactory){
               NamedThreadFactory namedThreadFactory = (NamedThreadFactory) threadFactory;
               ThreadPoolMonitorData.add(namedThreadFactory.getName(),namedThreadFactory.getDesc(), executor);
           }else {
               ThreadPoolMonitorData.add(executor.getClass().getName()+"@"+executor.hashCode(),"未使用提供的NamedThreadFactory", executor);
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }
}