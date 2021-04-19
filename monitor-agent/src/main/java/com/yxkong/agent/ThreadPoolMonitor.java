/**
  * 
  * @Author:   yxk
  * @Date:     2021/4/2 下午12:28
  * @version:  1.0
  */
package com.yxkong.agent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 〈〉
 *
 * @author ducongcong
 * @create 2021/4/18
 * @since 1.0.0
 */
public class ThreadPoolMonitor {
    private static final Map<String, ThreadPoolExecutor> threadPoolExecutorMap = new HashMap<>();

    public Map<String, ThreadPoolExecutor> alls(){
        return threadPoolExecutorMap;
    }
    /**
     * 新增ThreadPoolExecutor
     * @param key
     * @param executor
     */
    public void add(String key,ThreadPoolExecutor executor){
        if(!threadPoolExecutorMap.containsKey(key)){
            threadPoolExecutorMap.put(key,executor);
        }
    }
    public  void remove(String key){
        threadPoolExecutorMap.remove(key);
    }
    public  ThreadPoolExecutor get(String key){
        return threadPoolExecutorMap.get(key);
    }

}
