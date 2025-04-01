package com.jasper.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * @description: 读取配置文件参数，创建线程池
 * @create: 2025-03-31 17:53
 * @author: zihong
 **/
@Slf4j
@EnableAsync
@Configuration
@EnableConfigurationProperties(ThreadPoolConfigProperties.class)
public class ThreadPoolConfig {


    /**
     * AbortPolicy：丢弃任务并抛出RejectedExecutionException异常。
     * DiscardPolicy：直接丢弃任务，但是不会抛出异常
     * DiscardOldestPolicy：将最早进入队列的任务删除，之后再尝试加入队列的任务被拒绝
     * CallerRunsPolicy：如果任务添加线程池失败，那么主线程自己执行该任务
     */
    public static final String ABORT_POLICY = "AbortPolicy";
    public static final String DISCARD_POLICY = "DiscardPolicy";
    public static final String DISCARD_OLDEST_POLICY = "DiscardOldestPolicy";
    public static final String CALLER_RUNS_POLICY = "CallerRunsPolicy";


    /**
     * 创建线程池
     *
     * @param properties 利用Spring的自动装配机制来进行方法参数的解析，从容器中获取
     * @return
     */
    @Bean("threadPoolExecutor01")
    public ThreadPoolExecutor threadPoolExecutor01(ThreadPoolConfigProperties properties) {
        RejectedExecutionHandler handler = null;
        switch (properties.getPolicy()) {
            case ABORT_POLICY:
                handler = new ThreadPoolExecutor.AbortPolicy();
                break;
            case DISCARD_POLICY:
                handler = new ThreadPoolExecutor.DiscardPolicy();
                break;
            case DISCARD_OLDEST_POLICY:
                handler = new ThreadPoolExecutor.DiscardOldestPolicy();
                break;
            case CALLER_RUNS_POLICY:
                handler = new ThreadPoolExecutor.CallerRunsPolicy();
                break;
            default:
                handler = new ThreadPoolExecutor.AbortPolicy();
                break;
        }

        return new ThreadPoolExecutor(
                properties.getCorePoolSize(),
                properties.getMaxPoolSize(),
                properties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(properties.getBlockQueueSize()),
                Executors.defaultThreadFactory(),
                handler
        );

    }

    /**
     * 创建线程池-02
     *
     * @param properties 利用Spring的自动装配机制来进行方法参数的解析，从容器中获取
     * @return
     */
    @Bean("threadPoolExecutor02")
    public ThreadPoolExecutor threadPoolExecutor02(ThreadPoolConfigProperties properties) {
        RejectedExecutionHandler handler = null;
        switch (properties.getPolicy()) {
            case ABORT_POLICY:
                handler = new ThreadPoolExecutor.AbortPolicy();
                break;
            case DISCARD_POLICY:
                handler = new ThreadPoolExecutor.DiscardPolicy();
                break;
            case DISCARD_OLDEST_POLICY:
                handler = new ThreadPoolExecutor.DiscardOldestPolicy();
                break;
            case CALLER_RUNS_POLICY:
                handler = new ThreadPoolExecutor.CallerRunsPolicy();
                break;
            default:
                handler = new ThreadPoolExecutor.AbortPolicy();
                break;
        }

        return new ThreadPoolExecutor(
                properties.getCorePoolSize(),
                properties.getMaxPoolSize(),
                properties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(properties.getBlockQueueSize()),
                Executors.defaultThreadFactory(),
                handler
        );

    }


}
