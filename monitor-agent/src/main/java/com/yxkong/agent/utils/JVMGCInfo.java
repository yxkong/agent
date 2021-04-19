package com.yxkong.agent.utils;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * gcInfo
 * @Author: yxkong
 * @Date: 2021/4/19 12:02 下午
 * @version: 1.0
 */
public class JVMGCInfo {

    private static  GarbageCollectorMXBean youngGC;
    private static  GarbageCollectorMXBean fullGC;

    static{
        List<GarbageCollectorMXBean> gcMXBeanList = ManagementFactory.getGarbageCollectorMXBeans();
        for (final GarbageCollectorMXBean gcMXBean : gcMXBeanList) {
            String gcName = gcMXBean.getName();
            if(gcName==null) {
                continue;
            }
            //G1 Old Generation
            //Garbage collection optimized for short pausetimes Old Collector
            //Garbage collection optimized for throughput Old Collector
            //Garbage collection optimized for deterministic pausetimes Old Collector
            //G1 Young Generation
            //Garbage collection optimized for short pausetimes Young Collector
            //Garbage collection optimized for throughput Young Collector
            //Garbage collection optimized for deterministic pausetimes Young Collector
            if (fullGC == null &&
                    (gcName.endsWith("Old Collector") || "ConcurrentMarkSweep".equals(gcName) || "MarkSweepCompact".equals(gcName) || "PS MarkSweep".equals(gcName))
            ) {
                fullGC = gcMXBean;
            } else if (youngGC == null &&
                    (gcName.endsWith("Young Generation")|| "ParNew".equals(gcName) || "Copy".equals(gcName) || "PS Scavenge".equals(gcName))
            ) {
                youngGC = gcMXBean;
            }
        }
    }

    public static Map<String,Object> getInfo(){
        Map<String,Object> map = new HashMap<>();
        try {
            map.put("YoungGCName",getYoungGCName());
            map.put("YGCCount",getYoungGCCollectionCount());
            map.put("YGCTime",getYoungGCCollectionTime());
            map.put("fullGCName",getFullGCName());
            map.put("fullGCCount",getFullGCCollectionCount());
            map.put("fullGCTime",getFullGCCollectionTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    //YGC名称
    public static String getYoungGCName() {
        return youngGC == null ? "" : youngGC.getName();
    }

    //YGC总次数
    public static long getYoungGCCollectionCount() {
        return youngGC == null ? 0 : youngGC.getCollectionCount();
    }

    //YGC总时间
    public static  long getYoungGCCollectionTime() {
        return youngGC == null ? 0 : youngGC.getCollectionTime();
    }

    //FGC名称
    public static  String getFullGCName() {
        return fullGC == null ? "" : fullGC.getName();
    }

    //FGC总次数
    public static  long getFullGCCollectionCount() {
        return fullGC == null ? 0 : fullGC.getCollectionCount();
    }

    //FGC总次数
    public static  long getFullGCCollectionTime() {
        return fullGC == null ? 0 : fullGC.getCollectionTime();
    }
}