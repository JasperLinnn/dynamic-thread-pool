package com.jasper.middleware.dynamic.thread.pool.sdk.config;

import com.alibaba.fastjson2.JSON;
import com.esotericsoftware.minlog.Log;
import com.jasper.middleware.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import com.jasper.middleware.dynamic.thread.pool.sdk.domain.impl.DynamicThreadPoolServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description 动态配置入口
 * @author zihong
 * @date 2025/3/31 16:20
 **/
@Configuration
public class DynamicThreadPoolAutoConfig {

    private final Logger log = LoggerFactory.getLogger(DynamicThreadPoolAutoConfig.class);
    @Bean("dynamicThreadPoolService")
    public IDynamicThreadPoolService dynamicThreadPoolService(ApplicationContext applicationContext, Map<String, ThreadPoolExecutor> threadPoolExecutorMap) {
        String applicationName = applicationContext.getEnvironment().getProperty("spring.application.name");
        if (StringUtils.isEmpty(applicationName)) {
            applicationName = "default";
            log.warn("applicationName is empty, use default");
        }
        return new DynamicThreadPoolServiceImpl(applicationName, threadPoolExecutorMap);
    }

}
