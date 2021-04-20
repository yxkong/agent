### com.yxkong.agent目录结构
#### ByteBuddyAgent  为主类
- 增强ThreadPoolExecutor类
- 初始化httpServer

#### advice  为 具体的增强
#### dto 为传输包装
#### httpserver 
- 实现了http服务（参考zabbix的jmx_exporter）
- 功能服务必须继承Collector，同时在构造方法里设置methodName，
- 实现collect 接口，就是具体要干的事，以及对应的返回参数
- 在ByteBuddyAgent.initHttpServer()方法中实例化一个对象并且register()
如  new ThreadPoolCollector().register();
  
### 主要实现功能
- JVMCollector  jvm的监控
- StackCollector 当前栈信息收集
- ThreadPoolCollector 线程池收集
- ThreadPoolModify  线程池部分信息修改
### 注意事项：
- 线程池的ThreadFactory最好使用bootstrap-load-client-0.1.jar的NamedThreadFactory
- 否则无法获取线程池的名称和描述
- 针对没有使用NamedThreadFactory的ThreadPoolExecutor使用对象的hashCode做为key

### 测试
MonitorTest
![单元测试配置](./doc/img.png)
控制台输出：
```
获取ThreadPoolMonitorData的类加载器：null   bootstrap classLoader 被屏蔽了，返回的是null
获取ThreadPoolMonitorData收集的线程池：{}
获取ThreadPoolMonitor的类加载器：sun.misc.Launcher$AppClassLoader@18b4aac2
线程池pool执行中1:biz-pool-3-thread-1
线程池pool执行中2:biz-pool-3-thread-2
线程池pool执行中3:biz-pool-3-thread-3
线程池executorService执行中1:pool-1-thread-1
线程池启动完再获取收集的线程池：
{"616881582":{"executor":{"activeCount":0,"threadFactory":{},"largestPoolSize":1,"poolSize":1,"taskCount":1,"rejectedExecutionHandler":{},"corePoolSize":0,"completedTaskCount":1,"terminating":false,"maximumPoolSize":2147483647,"queue":[],"shutdown":false,"terminated":false},"name":"616881582","desc":"未使用提供的NamedThreadFactory"},"biz":{"executor":{"activeCount":0,"threadFactory":{"name":"biz","desc":"业务执行线程池"},"largestPoolSize":3,"poolSize":3,"taskCount":3,"rejectedExecutionHandler":{},"corePoolSize":5,"completedTaskCount":3,"terminating":false,"maximumPoolSize":10,"queue":[],"shutdown":false,"terminated":false},"name":"biz","desc":"业务执行线程池"}}
```

jvm 数据获取
http://10.214.12.38:8090/jvm
```
{
    "data": {
        "JVM": {
            "specVersion": "1.8",
            "unLoadedClassCount": 0,
            "javaVersion": "1.8.0_281",
            "jitName": "HotSpot 64-Bit Tiered Compilers",
            "jitTime": 1,
            "pid": "10866",
            "totalLoadedClassCount": 3038,
            "version": "25.281-b09",
            "specVendor": "Oracle Corporation",
            "specName": "Java Virtual Machine Specification",
            "loadedClassCount": 3038,
            "vendor": "Oracle Corporation",
            "name": "Java HotSpot(TM) 64-Bit Server VM",
            "startTime": 1618885248,
            "runTime": 19
        },
        "memory": {
            "oldGenMemoryUsage: ": {
                "init": 179306496,
                "usedPercent": 5.7213474E-6,
                "committed": 179306496,
                "max": 2863661056,
                "used": 16384
            },
            "permGenMemoryUsage: ": {
                "init": 0,
                "usedPercent": 0.0,
                "committed": 16646144,
                "max": -1,
                "used": 15378040
            },
            "permGenMemoryPeakUsage: ": {
                "init": 0,
                "usedPercent": 0.0,
                "committed": 16646144,
                "max": -1,
                "used": 15378072
            },
            "nonHeapMemoryUsage": {
                "init": 2555904,
                "usedPercent": 0.0,
                "committed": 23134208,
                "max": -1,
                "used": 21547992
            },
            "edenSpaceMemoryUsage": {
                "init": 67108864,
                "usedPercent": 0.011954648,
                "committed": 67108864,
                "max": 1409286144,
                "used": 16847520
            },
            "codeCacheMemoryUsage: ": {
                "init": 2555904,
                "usedPercent": 0.015820567,
                "committed": 3997696,
                "max": 251658240,
                "used": 3981376
            },
            "heapMemoryUsage": {
                "init": 268435456,
                "usedPercent": 0.0064246687,
                "committed": 257425408,
                "max": 3817865216,
                "used": 24528520
            },
            "oldGenMemoryPeakUsage: ": {
                "init": 179306496,
                "usedPercent": 5.7213474E-6,
                "committed": 179306496,
                "max": 2863661056,
                "used": 16384
            },
            "codeCacheMemoryPeakUsage: ": {
                "init": 2555904,
                "usedPercent": 0.015820567,
                "committed": 3997696,
                "max": 251658240,
                "used": 3981376
            },
            "survivorSpaceMemoryUsage: ": {
                "init": 11010048,
                "usedPercent": 0.72662354,
                "committed": 11010048,
                "max": 11010048,
                "used": 8000160
            },
            "survivorSpaceMemoryPeakUsage: ": {
                "init": 11010048,
                "usedPercent": 0.72662354,
                "committed": 11010048,
                "max": 11010048,
                "used": 8000160
            },
            "edenSpaceMemoryPeakUsage": {
                "init": 67108864,
                "usedPercent": 0.04761905,
                "committed": 67108864,
                "max": 1409286144,
                "used": 67108864
            }
        },
        "thread": {
            "threadCount": 13,
            "resetPeakThreadCount": 13,
            "daemonThreadCount": 5,
            "peakThreadCount": 13
        },
        "GC": {
            "fullGCName": "PS MarkSweep",
            "YGCTime": 5,
            "fullGCTime": 0,
            "fullGCCount": 0,
            "YGCCount": 1,
            "YoungGCName": "PS Scavenge"
        }
    },
    "message": "执行成功！",
    "status": "1",
    "timestamp": 1618885268328
}
```
线程池数据获取
http://10.214.12.38:8090/threadPool
```
{
    "data": {
        "execute-pool-1": {
            "executor": {
                "activeCount": 1,
                "threadFactory": {
                    "name": "execute-pool-1",
                    "desc": "default"
                },
                "largestPoolSize": 1,
                "poolSize": 1,
                "taskCount": 1,
                "rejectedExecutionHandler": {},
                "corePoolSize": 5,
                "completedTaskCount": 0,
                "terminating": false,
                "maximumPoolSize": 5,
                "queue": [],
                "shutdown": false,
                "terminated": false
            },
            "name": "execute-pool-1",
            "desc": "default"
        },
        "biz": {
            "executor": {
                "activeCount": 0,
                "threadFactory": {
                    "name": "biz",
                    "desc": "业务执行线程池"
                },
                "largestPoolSize": 3,
                "poolSize": 3,
                "taskCount": 3,
                "rejectedExecutionHandler": {},
                "corePoolSize": 5,
                "completedTaskCount": 3,
                "terminating": false,
                "maximumPoolSize": 10,
                "queue": [],
                "shutdown": false,
                "terminated": false
            },
            "name": "biz",
            "desc": "业务执行线程池"
        },
        "330551672": {
            "executor": {
                "activeCount": 0,
                "threadFactory": {},
                "largestPoolSize": 1,
                "poolSize": 1,
                "taskCount": 1,
                "rejectedExecutionHandler": {},
                "corePoolSize": 0,
                "completedTaskCount": 1,
                "terminating": false,
                "maximumPoolSize": 2147483647,
                "queue": [],
                "shutdown": false,
                "terminated": false
            },
            "name": "330551672",
            "desc": "未使用提供的NamedThreadFactory"
        }
    },
    "message": "执行成功！",
    "status": "1",
    "timestamp": 1618885262814
}
```
线程池修改
http://10.214.12.38:8090/threadPool/modify?key=execute-pool-1&coreSize=3&maximumPoolSize=100
```
{
    "data": {
        "activeCount": 1,
        "threadFactory": {
            "name": "execute-pool-1",
            "desc": "default"
        },
        "largestPoolSize": 3,
        "poolSize": 3,
        "taskCount": 1,
        "rejectedExecutionHandler": {},
        "corePoolSize": 3,
        "completedTaskCount": 0,
        "terminating": false,
        "maximumPoolSize": 100,
        "queue": [],
        "shutdown": false,
        "terminated": false
    },
    "message": "执行成功！",
    "status": "1",
    "timestamp": 1618885771574
}
```

