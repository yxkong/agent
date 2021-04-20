package com.yxkong.agent;

import com.yxkong.agent.advice.ThreadPoolExecutorExecuteAdvice;
import com.yxkong.agent.advice.ThreadPoolExecutorFinalizeAdvice;
import com.yxkong.agent.httpserver.*;
import com.yxkong.agent.httpserver.collector.CollectorRegistry;
import com.yxkong.agent.utils.InetAddrUtil;
import com.yxkong.agent.utils.StringUtils;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * TODO
 *
 * @author yxkong
 * @version 1.0
 * @date 2021/3/25 17:20
 */
public class ByteBuddyAgent {
    static HTTPServer server;
    public static void agentmain(String args, Instrumentation inst) {
        premain(args, inst);
    }
    public static void premain(final String args, final Instrumentation instrumentation) {
        try {
            threadPoolExecutor(instrumentation);
            String host = "";
            Integer port = null;
            if(StringUtils.isNotEmpty(args)){
                String[] hosts = args.split(",");
                host = hosts[0];
                port = Integer.parseInt(hosts[1]);
            }

            initHttpServer(host,port);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void initHttpServer(String host,Integer port){
        try {
            new StackCollector().register();
            new JVMCollector().register();
            new ThreadPoolCollector().register();
            new ThreadPoolModify().register();
            if(port == null){
                port = 8090;
            }
            if(StringUtils.isEmpty(host)) {
                host = InetAddrUtil.getHost();
            }
            InetSocketAddress socket = new InetSocketAddress(host, port);
            server = new HTTPServer(socket, CollectorRegistry.defaultRegistry, true);
            System.out.println(String.format("启动server：%s:%d",host,port ));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    /**
     * 针对threadPoolExecutor 的增强
     * @param instrumentation
     */
    private static void threadPoolExecutor(Instrumentation instrumentation){
        new AgentBuilder.Default()
                .disableClassFormatChanges()
                //默认是不对bootstrap类加载器加载的对象instrumentation，忽略某个type后，就可以了
                .ignore(ElementMatchers.noneOf(ThreadPoolExecutor.class))
                //
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                //
                .with(AgentBuilder.RedefinitionStrategy.REDEFINITION)
                .with(AgentBuilder.TypeStrategy.Default.REDEFINE)
                .with(AgentBuilder.InjectionStrategy.UsingUnsafe.INSTANCE)
                .type(ElementMatchers.is(ThreadPoolExecutor.class))
                //.or(ElementMatchers.hasSuperType(ElementMatchers.named("java.util.concurrent.Executor")))
                //.or(ElementMatchers.hasSuperType(ElementMatchers.named("java.util.concurrent.ExecutorService")))
                .transform((builder, typeDescription, classLoader, javaModule) ->
                        builder.visit(Advice.to(ThreadPoolExecutorFinalizeAdvice.class)
                                    .on(ElementMatchers.named("finalize")
                                    .or(ElementMatchers.named("shutdown"))))
                                .visit(Advice.to(ThreadPoolExecutorExecuteAdvice.class).on(ElementMatchers.named("execute")))
                )
                .installOn(instrumentation);
    }

}
