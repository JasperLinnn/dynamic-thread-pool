package com.jasper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: 从配置文件中读取线程池的配置的参数
 * @create: 2025-03-30 17:48
 * @author: zihong
 **/
@Data
@ConfigurationProperties(prefix = "thread.pool.executor.config")
public class ThreadPoolConfigProperties {
    /**
     * 核心线程数
     * core-pool-size: 20
     */
    private Integer corePoolSize;
    /**
     * 最大线程数
     * max-pool-size: 50
     */
    private Integer maxPoolSize;
    /**
     * 空闲线程的存活时间
     * keep-alive-time: 5000
     */
    private Long keepAliveTime;
    /**
     * 队列容量
     * block-queue-size: 5000
     */
    private Integer blockQueueSize;

    /**
     * AbortPolicy：丢弃任务并抛出RejectedExecutionException异常。
     * DiscardPolicy：直接丢弃任务，但是不会抛出异常
     * DiscardOldestPolicy：将最早进入队列的任务删除，之后再尝试加入队列的任务被拒绝
     * CallerRunsPolicy：如果任务添加线程池失败，那么主线程自己执行该任务
     */

    private String policy = "AbortPolicy";

}
