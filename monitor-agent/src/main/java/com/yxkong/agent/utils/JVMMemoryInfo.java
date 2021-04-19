package com.yxkong.agent.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: yxkong
 * @Date: 2021/4/19 12:23 下午
 * @version: 1.0
 */
public class JVMMemoryInfo {
    private static  MemoryMXBean memoryMXBean;
    private static  MemoryPoolMXBean edenSpaceMxBean;
    private static  MemoryPoolMXBean survivorSpaceMxBean;
    private static  MemoryPoolMXBean oldGenMxBean;
    private static  MemoryPoolMXBean permGenMxBean;
    private static  MemoryPoolMXBean codeCacheMxBean;

    public static Map<String,Object> getInfo(){
        Map<String,Object> map = new HashMap<>();

        try {
            map.put("heapMemoryUsage",getHeapMemoryUsage());
            map.put("nonHeapMemoryUsage",getNonHeapMemoryUsage());
            map.put("edenSpaceMemoryUsage" ,getEdenSpaceMemoryUsage());
            map.put("edenSpaceMemoryPeakUsage" ,getAndResetEdenSpaceMemoryPeakUsage());
            map.put("survivorSpaceMemoryUsage: " ,getSurvivorSpaceMemoryUsage());
            map.put("survivorSpaceMemoryPeakUsage: " ,getAndResetSurvivorSpaceMemoryPeakUsage());
            map.put("oldGenMemoryUsage: " ,getOldGenMemoryUsage());
            map.put("oldGenMemoryPeakUsage: " ,getAndResetOldGenMemoryPeakUsage());
            map.put("permGenMemoryUsage: " ,getPermGenMemoryUsage());
            map.put("permGenMemoryPeakUsage: " ,getAndResetPermGenMemoryPeakUsage());
            map.put("codeCacheMemoryUsage: " ,getCodeCacheMemoryUsage());
            map.put("codeCacheMemoryPeakUsage: " ,getAndResetCodeCacheMemoryPeakUsage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    /**
     * JVM内存区域使用情况。</br>
     * <pre>
     * init：初始内存大小（字节）
     * used：当前使用内存大小（字节）
     * committed：已经申请分配的内存大小（字节）
     * max：最大内存大小（字节）
     * usedPercent：已经申请分配内存与最大内存大小的百分比
     * </pre>
     * @author tangjiyu
     */
    public static  class JVMMemoryUsage {
        //初始内存大小（字节）
        private long init;
        //当前使用内存大小（字节）
        private long used;
        //已经申请分配的内存大小（字节）
        private long committed;
        //最大内存大小（字节）
        private long max;
        //已经申请分配内存与最大内存大小的百分比
        private float usedPercent;

        public JVMMemoryUsage(MemoryUsage memoryUsage) {
            this.setMemoryUsage(memoryUsage);
            //this(memoryUsage.getInit(), memoryUsage.getUsed(), memoryUsage.getCommitted(), memoryUsage.getMax());
        }

        public JVMMemoryUsage(long init, long used, long committed, long max) {
            super();
            this.setMemoryUsage(init, used, committed, max);
        }

        private void setMemoryUsage(MemoryUsage memoryUsage) {
            if(memoryUsage!=null) {
                this.setMemoryUsage(memoryUsage.getInit(), memoryUsage.getUsed(), memoryUsage.getCommitted(), memoryUsage.getMax());
            } else {
                this.setMemoryUsage(0, 0, 0, 0);
            }
        }

        private void setMemoryUsage(long init, long used, long committed, long max) {
            this.init = init;
            this.used = used;
            this.committed = committed;
            this.max = max;
            if(this.used>0 && max>0) {
                this.usedPercent = used * Float.valueOf("1.0") / max;
            } else {
                this.usedPercent = 0;
            }
        }

        public long getInit() {
            return init;
        }
        public long getUsed() {
            return used;
        }
        public long getCommitted() {
            return committed;
        }
        public long getMax() {
            return max;
        }
        public float getUsedPercent() {
            return usedPercent;
        }

        @Override
        public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append("init = " + init + "(" + (init >> 10) + "K) ");
            buf.append("used = " + used + "(" + (used >> 10) + "K) ");
            buf.append("committed = " + committed + "(" +
                    (committed >> 10) + "K) " );
            buf.append("max = " + max + "(" + (max >> 10) + "K)");
            buf.append("usedPercent = " + usedPercent);
            return buf.toString();
        }
    }

    static {
        memoryMXBean = ManagementFactory.getMemoryMXBean();

        List<MemoryPoolMXBean> memoryPoolMXBeanList = ManagementFactory.getMemoryPoolMXBeans();
        for (final MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeanList) {
            String poolName = memoryPoolMXBean.getName();
            if(poolName==null) {
                continue;
            }
            // 官方JVM(HotSpot)提供的MemoryPoolMXBean
            // JDK1.7/1.8 Eden区内存池名称： "Eden Space" 或  "PS Eden Space"、 “G1 Eden Space”(和垃圾收集器有关)
            // JDK1.7/1.8 Survivor区内存池名称："Survivor Space" 或 "PS Survivor Space"、“G1 Survivor Space”(和垃圾收集器有关)
            // JDK1.7  老区内存池名称： "Tenured Gen"
            // JDK1.8  老区内存池名称："Old Gen" 或 "PS Old Gen"、“G1 Old Gen”(和垃圾收集器有关)
            // JDK1.7  方法/永久区内存池名称： "Perm Gen" 或 "PS Perm Gen"(和垃圾收集器有关)
            // JDK1.8  方法/永久区内存池名称："Metaspace"(注意：不在堆内存中)
            // JDK1.7/1.8  CodeCache区内存池名称： "Code Cache"
            if (edenSpaceMxBean==null && poolName.endsWith("Eden Space")) {
                edenSpaceMxBean = memoryPoolMXBean;
            } else if (survivorSpaceMxBean==null && poolName.endsWith("Survivor Space")) {
                survivorSpaceMxBean = memoryPoolMXBean;
            } else if (oldGenMxBean==null && (poolName.endsWith("Tenured Gen") || poolName.endsWith("Old Gen"))) {
                oldGenMxBean = memoryPoolMXBean;
            } else if (permGenMxBean==null && (poolName.endsWith("Perm Gen") || poolName.endsWith("Metaspace"))) {
                permGenMxBean = memoryPoolMXBean;
            }  else if (codeCacheMxBean==null && poolName.endsWith("Code Cache")) {
                codeCacheMxBean = memoryPoolMXBean;
            }
        }
    }// static


    /**
     * 获取堆内存情况
     * @return 不能获取到返回null
     */
    public static  JVMMemoryUsage getHeapMemoryUsage() {
        if(memoryMXBean!=null) {
            final MemoryUsage usage =memoryMXBean.getHeapMemoryUsage();
            if(usage!=null) {
                return new JVMMemoryUsage(usage);
            }
        }
        return null;
    }

    /**
     * 获取堆外内存情况
     * @return 不能获取到返回null
     */
    public static  JVMMemoryUsage getNonHeapMemoryUsage() {
        if(memoryMXBean!=null) {
            final MemoryUsage usage =memoryMXBean.getNonHeapMemoryUsage();
            if(usage!=null) {
                return new JVMMemoryUsage(usage);
            }
        }
        return null;
    }

    /**
     * 获取Eden区内存情况
     * @return 不能获取到返回null
     */
    public static  JVMMemoryUsage getEdenSpaceMemoryUsage() {
        return getMemoryPoolUsage(edenSpaceMxBean);
    }

    /**
     * 获取Eden区内存峰值（从启动或上一次重置开始统计），并重置
     * @return 不能获取到返回null
     */
    public static  JVMMemoryUsage getAndResetEdenSpaceMemoryPeakUsage() {
        return getAndResetMemoryPoolPeakUsage(edenSpaceMxBean);
    }

    /**
     * 获取Survivor区内存情况
     * @return 不能获取到返回null
     */
    public static  JVMMemoryUsage getSurvivorSpaceMemoryUsage() {
        return getMemoryPoolUsage(survivorSpaceMxBean);
    }

    /**
     * 获取Survivor区内存峰值（从启动或上一次重置开始统计），并重置
     * @return 不能获取到返回null
     */
    public static  JVMMemoryUsage getAndResetSurvivorSpaceMemoryPeakUsage() {
        return getAndResetMemoryPoolPeakUsage(survivorSpaceMxBean);
    }

    /**
     * 获取老区内存情况
     * @return 不能获取到返回null
     */
    public static  JVMMemoryUsage getOldGenMemoryUsage() {
        return getMemoryPoolUsage(oldGenMxBean);
    }

    /**
     * 获取老区内存峰值（从启动或上一次重置开始统计），并重置
     * @return 不能获取到返回null
     */
    public static  JVMMemoryUsage getAndResetOldGenMemoryPeakUsage() {
        return getAndResetMemoryPoolPeakUsage(oldGenMxBean);
    }

    /**
     * 获取永久区/方法区内存情况
     * @return 不能获取到返回null
     */
    public static  JVMMemoryUsage getPermGenMemoryUsage() {
        return getMemoryPoolUsage(permGenMxBean);
    }

    /**
     * 获取永久区/方法区内存峰值（从启动或上一次重置开始统计），并重置
     * @return 不能获取到返回null
     */
    public static  JVMMemoryUsage getAndResetPermGenMemoryPeakUsage() {
        return getAndResetMemoryPoolPeakUsage(permGenMxBean);
    }

    /**
     * 获取CodeCache区内存情况
     * @return 不能获取到返回null
     */
    public static  JVMMemoryUsage getCodeCacheMemoryUsage() {
        return getMemoryPoolUsage(codeCacheMxBean);
    }

    /**
     * 获取CodeCache区内存峰值（从启动或上一次重置开始统计），并重置
     * @return 不能获取到返回null
     */
    public static  JVMMemoryUsage getAndResetCodeCacheMemoryPeakUsage() {
        return getAndResetMemoryPoolPeakUsage(codeCacheMxBean);
    }

    private static  JVMMemoryUsage getMemoryPoolUsage(MemoryPoolMXBean memoryPoolMXBean) {
        if(memoryPoolMXBean!=null) {
            final MemoryUsage usage = memoryPoolMXBean.getUsage();
            if(usage!=null) {
                return new JVMMemoryUsage(usage);
            }
        }
        return null;
    }

    private static  JVMMemoryUsage getAndResetMemoryPoolPeakUsage(MemoryPoolMXBean memoryPoolMXBean) {
        if(memoryPoolMXBean!=null) {
            final MemoryUsage usage = memoryPoolMXBean.getPeakUsage();
            if(usage!=null) {
                memoryPoolMXBean.resetPeakUsage();
                return new JVMMemoryUsage(usage);
            }
        }
        return null;
    }
}