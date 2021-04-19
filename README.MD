### 说明
#### bootstrap-load-client 
此模块需要打成jar包
- 引入项目中；
- 配置bootstrap ClassLoader加载

#### monitor-agent 
此模块需要打成jar包，当一个agent使用
启动参数配置
```
-javaagent:/Users/yxk/git/agent/monitor-agent/target/monitor-agent-0.1.jar=127.0.0.1,8888 -Xbootclasspath/a:/Users/yxk/git/agent/bootstrap-load-client/target/bootstrap-load-client-0.1.jar
```
