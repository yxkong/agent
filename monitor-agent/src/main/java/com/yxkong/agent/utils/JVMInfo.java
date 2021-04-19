package com.yxkong.agent.utils;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: yxkong
 * @Date: 2021/4/19 12:11 下午
 * @version: 1.0
 */
public class JVMInfo {
    private static RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
    private static  ClassLoadingMXBean classLoad = ManagementFactory.getClassLoadingMXBean();
    //可能为null
    private static  CompilationMXBean compilation = ManagementFactory.getCompilationMXBean();
    private static  Properties properties = System.getProperties();

    public static Map<String,Object> getInfo(){
        Map<String,Object> map = new HashMap<>();
        try {
            map.put("pid",getPID());
            map.put("specName",getJVMSpecName());
            map.put("specVendor",getJVMSpecVendor());
            map.put("specVersion",getJVMSpecVersion());
            map.put("javaVersion",getJavaVersion());
            map.put("name",getJVMName());
            map.put("vendor",getJVMVendor());
            map.put("version",getJVMVersion());
            map.put("startTime",getJVMStartTimeMs()/1000);
            map.put("runTime",getJVMUpTimeMs()/1000);
            map.put("loadedClassCount",getJVMLoadedClassCount());
            map.put("unLoadedClassCount",getJVMUnLoadedClassCount());
            map.put("totalLoadedClassCount",getJVMTotalLoadedClassCount());
            map.put("jitName",getJITName());
            map.put("jitTime",getJITTimeMs()/1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    /**
     * 获取JVM进程PID
     * @return
     */
    public static String getPID() {
        String pid = System.getProperty("pid");
        if (pid == null) {
            String name = runtime.getName();
            if(name != null) {
                pid = name.split("@")[0];
                System.setProperty("pid", pid);
            }
        }
        return pid;
    }

    /**
     * 获取JVM规范名称
     * @return
     */
    public static  String getJVMSpecName() {
        return runtime.getSpecName();
    }

    /**
     * 获取JVM规范运营商
     * @return
     */
    public static  String getJVMSpecVendor() {
        return runtime.getSpecVendor();
    }

    /**
     * 获取JVM规范版本（如：1.7）
     * @return
     */
    public static  String getJVMSpecVersion() {
        return runtime.getSpecVersion();
    }

    /**
     * 获取JVM名称
     * @return
     */
    public static  String getJVMName() {
        return runtime.getVmName();
    }

    /**
     * 获取Java的运行环境版本（如：1.7.0_67）
     * @return
     */
    public static  String getJavaVersion() {
        return getSystemProperty("java.version");
    }

    /**
     * 获取JVM运营商
     * @return
     */
    public static  String getJVMVendor() {
        return runtime.getVmVendor();
    }

    /**
     * 获取JVM实现版本（如：25.102-b14）
     * @return
     */
    public static  String getJVMVersion() {
        return runtime.getVmVersion();
    }

    /**
     * 获取JVM启动时间
     * @return
     */
    public static  long getJVMStartTimeMs() {
        return runtime.getStartTime();
    }

    /**
     * 获取JVM运行时间
     * @return
     */
    public static  long getJVMUpTimeMs() {
        return runtime.getUptime();
    }

    /**
     * 获取JVM当前加载类总量
     * @return
     */
    public static  long getJVMLoadedClassCount() {
        return classLoad.getLoadedClassCount();
    }

    /**
     * 获取JVM已卸载类总量
     * @return
     */
    public static  long getJVMUnLoadedClassCount() {
        return classLoad.getUnloadedClassCount();
    }

    /**
     * 获取JVM从启动到现在加载类总量
     * @return
     */
    public static  long getJVMTotalLoadedClassCount() {
        return classLoad.getTotalLoadedClassCount();
    }

    /**
     * 获取JIT编译器名称
     * @return
     */
    public static  String getJITName() {
        return null == compilation ? "" : compilation.getName();
    }

    /**
     * 获取JIT总编译时间
     * @return
     */
    public static  long getJITTimeMs() {
        if (null!=compilation && compilation.isCompilationTimeMonitoringSupported()) {
            return compilation.getTotalCompilationTime();
        }
        return -1;
    }

    /**
     * 获取指定key的属性值
     * @param key
     * @return
     */
    public static  String getSystemProperty(String key) {
        return properties.getProperty(key);
    }

    public static  Properties getSystemProperty() {
        return properties;
    }
}