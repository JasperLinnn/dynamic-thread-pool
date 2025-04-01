package com.jasper.middleware.dynamic.thread.pool.sdk.trigger.job;

import com.alibaba.fastjson2.JSON;
import com.jasper.middleware.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import com.jasper.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.jasper.middleware.dynamic.thread.pool.sdk.registry.IRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * @author zihong
 * @description 线程池数据上报任务
 * @create 2025/4/1 18:04
 **/
public class ThreadPoolDataReportJob {
    private Logger logger = LoggerFactory.getLogger(ThreadPoolDataReportJob.class);

    private final IDynamicThreadPoolService dynamicThreadPoolService;

    private final IRegistry registry;

    public ThreadPoolDataReportJob(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
        this.dynamicThreadPoolService = dynamicThreadPoolService;
        this.registry = registry;
    }

    /**
     * 20秒上报一次线程池信息
     */
    @Scheduled(cron = "0/20 * * * * ?")
    public void execute() {
        logger.info("ThreadPoolDataReportJob execute");

        // 获取线程池配置列表
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.queryThreadPoolList();

        // 上报线程池配置列表
        registry.reportThreadPool(threadPoolConfigEntities);
        logger.info("动态线程池，上报线程池信息:{}", JSON.toJSONString(threadPoolConfigEntities));

        // 获取线程池配置参数
        threadPoolConfigEntities.forEach(threadPoolConfigEntity -> {
            registry.reportThreadPoolConfigParameter(threadPoolConfigEntity);
            logger.info("动态线程池，上报线程池配置:{}", JSON.toJSONString(threadPoolConfigEntity));
        });
    }
}
