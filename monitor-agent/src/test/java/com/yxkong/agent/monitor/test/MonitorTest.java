package com.yxkong.agent.monitor.test;

import com.alibaba.fastjson.JSON;
import com.yxkong.agent.NamedThreadFactory;
import com.yxkong.agent.ThreadPoolMonitor;
import com.yxkong.agent.data.ThreadPoolMonitorData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.*;

/**
 * @Author: yxkong
 * @Date: 2021/4/18 5:08 下午
 * @version: 1.0
 */
public class MonitorTest {
    @Test
    public void ip(){
        try {
            InetAddress ip4 = Inet4Address.getLocalHost();
            System.out.println(ip4.getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("线程池监控测试")
    public void test(){
        System.out.println("获取ThreadPoolMonitorData的类加载器："+ThreadPoolMonitorData.class.getClassLoader());
        System.out.println("获取ThreadPoolMonitorData收集的线程池："+ThreadPoolMonitorData.alls());
        System.out.println("获取ThreadPoolMonitor的类加载器："+ThreadPoolMonitor.class.getClassLoader());
        ThreadPoolExecutor pool= threadpool();
        pool.submit(()->{
            System.out.println("线程池pool执行中1:"+Thread.currentThread().getName());
        });
        pool.submit(()->{
            System.out.println("线程池pool执行中2:"+Thread.currentThread().getName());
        });
        pool.submit(()->{
            System.out.println("线程池pool执行中3:"+Thread.currentThread().getName());
        });

        ExecutorService executorService =  threadpool1();
        executorService.submit(()->{
            System.out.println("线程池executorService执行中1:"+Thread.currentThread().getName());
        });


        System.out.println("线程池启动完再获取收集的线程池：\r\n"+JSON.toJSON(ThreadPoolMonitorData.alls()));

        //ThreadPoolMonitorData.alls().forEach((key,val) ->{
        //    System.out.println("ThreadPoolMonitorData key="+key+" val:"+val);
        //});
        //executorService.shutdownNow();
        //ThreadPoolMonitor monitor = new ThreadPoolMonitor();
        //monitor.alls().forEach((key,val)->{
        //    System.out.println("ThreadPoolMonitor key="+key+" val:"+val);
        //});

        try {
            Thread.currentThread().join(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private ThreadPoolExecutor threadpool(){
        ThreadPoolExecutor pool =  new ThreadPoolExecutor(5,
                10,
                200,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10),
                new NamedThreadFactory.Builder().name("biz").desc("业务执行线程池").build()
        );
        return pool;
    }
    private  ExecutorService threadpool1(){
        return Executors.newCachedThreadPool();
    }
}