package com.yxkong.agent.httpserver;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.yxkong.agent.NamedThreadFactory;
import com.yxkong.agent.httpserver.collector.CollectorRegistry;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * 自定义httpserver
 * */
public class HTTPServer {
    private static class LocalByteArray extends ThreadLocal<ByteArrayOutputStream> {
        @Override
        protected ByteArrayOutputStream initialValue()
        {
            return new ByteArrayOutputStream(1 << 20);
        }
    }

    /**
     * Handles  collections from the given registry.
     */
    static class HTTPHandler implements HttpHandler {
        private final CollectorRegistry registry;
        private final LocalByteArray response = new LocalByteArray();
        private final static String HEALTHY_RESPONSE = "is Healthy";

        HTTPHandler(CollectorRegistry registry) {
            this.registry = registry;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            String path = t.getRequestURI().getPath();
            String query = t.getRequestURI().getRawQuery();
            final Map<String, String> paramMap = parseQuery(query);
            try(OutputStream os = t.getResponseBody();){
                System.out.println(path);
                if ("/healthy".equals(path)) {
                    os.write(HEALTHY_RESPONSE.getBytes());
                } else {
                    //默认直接以json响应
                    t.getResponseHeaders().set("Content-Type", "application/json;charset=UTF-8");
                    if(path.contains("/")){
                        path = path.substring(1);
                    }
                    String data = registry.filteredCollector(path,paramMap);
                    t.sendResponseHeaders(HttpURLConnection.HTTP_OK, data.getBytes().length);
                    os.write(data.getBytes());
                }
                os.flush();
            }
            t.close();
        }

        /**
         * 过滤查询条件，暂时用不到
         * @param query
         * @return
         * @throws IOException
         */
        protected static Map<String,String> parseQuery(String query) throws IOException {
            Map<String,String> map = new HashMap<>();
            if (query != null) {
                String[] pairs = query.split("&");
                for (String pair : pairs) {
                   String[] arrays = pair.split("=");
                    map.put(arrays[0],arrays[1]);
                }
            }
            return map;
        }
    }

    protected final HttpServer server;
    protected final ExecutorService executorService;

    /**
     * 启动一个httpserver服务，并设置
     */
    public HTTPServer(HttpServer httpServer, CollectorRegistry registry, boolean daemon) throws IOException {
        if (httpServer.getAddress() == null) {
            throw new IllegalArgumentException("HttpServer hasn't been bound to an address");
        }

        server = httpServer;
        HttpHandler mHandler = new HTTPHandler(registry);
        server.createContext("/", mHandler);
        server.createContext("/metrics", mHandler);
        executorService =  Executors.newFixedThreadPool(5, NamedThreadFactory.defaultThreadFactory(daemon));
        server.setExecutor(executorService);
        start(daemon);
    }

    /**
     * Start a HTTP server serving Prometheus metrics from the given registry.
     */
    public HTTPServer(InetSocketAddress addr, CollectorRegistry registry, boolean daemon) throws IOException {
        this(HttpServer.create(addr, 3), registry, daemon);
    }

    /**
     * Start a HTTP server serving Prometheus metrics from the given registry using non-daemon threads.
     */
    public HTTPServer(InetSocketAddress addr, CollectorRegistry registry) throws IOException {
        this(addr, registry, false);
    }

    /**
     * Start a HTTP server serving the default Prometheus registry.
     */
    public HTTPServer(int port, boolean daemon) throws IOException {
        this(new InetSocketAddress(port), CollectorRegistry.defaultRegistry, daemon);
    }

    /**
     * Start a HTTP server serving the default Prometheus registry using non-daemon threads.
     */
    public HTTPServer(int port) throws IOException {
        this(port, false);
    }

    /**
     * Start a HTTP server serving the default Prometheus registry.
     */
    public HTTPServer(String host, int port, boolean daemon) throws IOException {
        this(new InetSocketAddress(host, port), CollectorRegistry.defaultRegistry, daemon);
    }

    /**
     * Start a HTTP server serving the default Prometheus registry using non-daemon threads.
     */
    public HTTPServer(String host, int port) throws IOException {
        this(new InetSocketAddress(host, port), CollectorRegistry.defaultRegistry, false);
    }

    /**
     * Start a HTTP server by making sure that its background thread inherit proper daemon flag.
     */
    private void start(boolean daemon) {
        if (daemon == Thread.currentThread().isDaemon()) {
            server.start();
        } else {
            FutureTask<Void> startTask = new FutureTask<Void>(new Runnable() {
                @Override
                public void run() {
                    server.start();
                }
            }, null);
            NamedThreadFactory.defaultThreadFactory(daemon).newThread(startTask).start();
            try {
                startTask.get();
            } catch (ExecutionException e) {
                throw new RuntimeException("Unexpected exception on starting HTTPSever", e);
            } catch (InterruptedException e) {
                // This is possible only if the current tread has been interrupted,
                // but in real use cases this should not happen.
                // In any case, there is nothing to do, except to propagate interrupted flag.
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Stop the HTTP server.
     */
    public void stop() {
        server.stop(0);
        executorService.shutdown(); // Free any (parked/idle) threads in pool
    }

    /**
     * Gets the port number.
     */
    public int getPort() {
        return server.getAddress().getPort();
    }
}

