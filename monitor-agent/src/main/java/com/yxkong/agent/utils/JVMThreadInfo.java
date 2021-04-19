package com.yxkong.agent.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: yxkong
 * @Date: 2021/4/19 12:33 下午
 * @version: 1.0
 */
public class JVMThreadInfo {
    private static ThreadMXBean threadMXBean;

    static {
        threadMXBean = ManagementFactory.getThreadMXBean();
    }
    public static Map<String,Object> getInfo() {
        Map<String,Object> map = new HashMap<>();
        try {
            map.put("daemonThreadCount",getDaemonThreadCount());
            map.put("threadCount",getThreadCount());
            map.put("peakThreadCount",getPeakThreadCount());
            map.put("resetPeakThreadCount",getAndResetPeakThreadCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    /**
     * Daemon线程总量
     * @return
     */
    public static int getDaemonThreadCount() {
        return threadMXBean.getDaemonThreadCount();
    }

    /**
     * 当前线程总量
     * @return
     */
    public static int getThreadCount() {
        return threadMXBean.getThreadCount();
    }

    /**
     * 获取线程数量峰值（从启动或resetPeakThreadCount()方法重置开始统计）
     * @return
     */
    public static int getPeakThreadCount() {
        return threadMXBean.getPeakThreadCount();
    }

    /**
     * 获取线程数量峰值（从启动或resetPeakThreadCount()方法重置开始统计），并重置
     * @return
     * @Throws java.lang.SecurityException - if a security manager exists and the caller does not have ManagementPermission("control").
     */
    public static int getAndResetPeakThreadCount() {
        int count = threadMXBean.getPeakThreadCount();
        resetPeakThreadCount();
        return count;
    }

    /**
     * 重置线程数量峰值
     * @Throws java.lang.SecurityException - if a security manager exists and the caller does not have ManagementPermission("control").
     */
    public static void resetPeakThreadCount() {
        threadMXBean.resetPeakThreadCount();
    }

    /**
     * 死锁线程总量
     * @return
     * @Throws IllegalStateException 没有权限或JVM不支持的操作
     */
    public static int getDeadLockedThreadCount() {
        try {
            long[] deadLockedThreadIds = threadMXBean.findDeadlockedThreads();
            if (deadLockedThreadIds == null) {
                return 0;
            }
            return deadLockedThreadIds.length;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}