package com.jasper.middleware.dynamic.thread.pool.sdk.config;

import com.jasper.middleware.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import com.jasper.middleware.dynamic.thread.pool.sdk.domain.impl.DynamicThreadPoolServiceImpl;
import com.jasper.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.jasper.middleware.dynamic.thread.pool.sdk.domain.model.vo.RegistryEnumVO;
import com.jasper.middleware.dynamic.thread.pool.sdk.registry.IRegistry;
import com.jasper.middleware.dynamic.thread.pool.sdk.registry.redis.RedisRegistry;
import com.jasper.middleware.dynamic.thread.pool.sdk.trigger.job.ThreadPoolDataReportJob;
import com.jasper.middleware.dynamic.thread.pool.sdk.trigger.listener.ThreadPoolConfigAdjustListener;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zihong
 * @description 动态配置入口
 * @date 2025/3/31 16:20
 **/
@Configuration
@EnableConfigurationProperties(DynamicThreadPoolAutoProperties.class)
@EnableScheduling
public class DynamicThreadPoolAutoConfig {

    private final Logger logger = LoggerFactory.getLogger(DynamicThreadPoolAutoConfig.class);

    /**
     * 项目名称
     */
    private String applicationName;

    /**
     * 线程池服务
     *
     * @param applicationContext    上下文
     * @param threadPoolExecutorMap 线程池集合
     */
    @Bean("dynamicThreadPoolService")
    public IDynamicThreadPoolService dynamicThreadPoolService(ApplicationContext applicationContext, Map<String, ThreadPoolExecutor> threadPoolExecutorMap, RedissonClient redissonClient) {
        applicationName = applicationContext.getEnvironment().getProperty("spring.application.name");
        if (StringUtils.isEmpty(applicationName)) {
            applicationName = "default";
            logger.warn("applicationName is empty, use default");
        }
        // 从注册中心中获取线程池配置
        Set<String> keys = threadPoolExecutorMap.keySet();
        for (String key : keys) {
            ThreadPoolConfigEntity configEntity =  redissonClient.<ThreadPoolConfigEntity>getBucket(String.format(RegistryEnumVO.THREAD_POOL_CONFIG_PARAMETER_LIST_KEY.getKey(), applicationName, key)).get();
            if (configEntity == null) {
                continue;
            }
            ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(key);
            threadPoolExecutor.setCorePoolSize(configEntity.getCorePoolSize());
            threadPoolExecutor.setMaximumPoolSize(configEntity.getMaximumPoolSize());
        }
        return new DynamicThreadPoolServiceImpl(applicationName, threadPoolExecutorMap);
    }

    /**
     * 使用Redis作为注册中心，引入Redisson
     * @param properties redis 配置
     */
    @Bean("dynamicThreadRedissonClient")
    public RedissonClient redissonClient(DynamicThreadPoolAutoProperties properties) {
        Config config = new Config();
        config.setCodec(JsonJacksonCodec.INSTANCE);

        config.useSingleServer()
                .setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
                .setPassword(properties.getPassword())
                .setConnectionPoolSize(properties.getPoolSize())
                .setConnectionMinimumIdleSize(properties.getMinIdleSize())
                .setIdleConnectionTimeout(properties.getIdleTimeout())
                .setConnectTimeout(properties.getConnectTimeout())
                .setRetryAttempts(properties.getRetryAttempts())
                .setRetryInterval(properties.getRetryInterval())
                .setPingConnectionInterval(properties.getPingInterval())
                .setKeepAlive(properties.isKeepAlive())
        ;

        RedissonClient redissonClient = Redisson.create(config);

        logger.info("动态线程池，注册器（redis）链接初始化完成。{} {} {}", properties.getHost(), properties.getPoolSize(), !redissonClient.isShutdown());

        return redissonClient;
    }

    /**
     * 注册中心
     *
     * @param redissonClient redis 注册中心
     */
    @Bean("redisRegistry")
    public IRegistry redisRegistry(RedissonClient redissonClient) {
        return new RedisRegistry(redissonClient);
    }

    /**
     * 线程池数据上报定时器
     *
     * @param dynamicThreadPoolService 线程池服务
     * @param registry                 注册中心
     */
    @Bean("threadPoolDataReportJob")
    public ThreadPoolDataReportJob threadPoolDataReportJob(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
        return new ThreadPoolDataReportJob(dynamicThreadPoolService, registry);
    }

    /**
     * 线程池配置调整监听器
     * @param dynamicThreadPoolService 线程池服务类
     * @param registry 注册中心
     */
    @Bean("threadPoolConfigAdjustListener")
    public ThreadPoolConfigAdjustListener threadPoolConfigAdjustListener(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
        return new ThreadPoolConfigAdjustListener(dynamicThreadPoolService, registry);
    }

    @Bean("dynamicThreadPoolTopic")
    public RTopic rTopic(RedissonClient redissonClient, ThreadPoolConfigAdjustListener threadPoolConfigAdjustListener) {
        RTopic rTopic = redissonClient.getTopic(RegistryEnumVO.DYNAMIC_THREAD_POOL_REDIS_TOPIC.getKey() + "_" + applicationName);
        rTopic.addListener(ThreadPoolConfigEntity.class, threadPoolConfigAdjustListener);
        return rTopic;
    }

}
