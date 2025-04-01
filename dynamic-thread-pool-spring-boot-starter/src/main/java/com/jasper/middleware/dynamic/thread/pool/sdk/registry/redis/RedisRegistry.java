package com.jasper.middleware.dynamic.thread.pool.sdk.registry.redis;

import com.jasper.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.jasper.middleware.dynamic.thread.pool.sdk.domain.model.vo.RegistryEnumVO;
import com.jasper.middleware.dynamic.thread.pool.sdk.registry.IRegistry;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;

import java.time.Duration;
import java.util.List;

/**
 * @author zihong
 * @description redis 注册中心实现
 * @create 2025/4/1 17:46
 **/
public class RedisRegistry implements IRegistry {

    private final RedissonClient redissonClient;

    public RedisRegistry(RedissonClient redisClient) {
        this.redissonClient = redisClient;
    }

    @Override
    public void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolConfigEntities) {
        RList<ThreadPoolConfigEntity> list = redissonClient.getList(RegistryEnumVO.THREAD_POOL_CONFIG_LIST_KEY.getKey());
        list.delete();
        list.addAll(threadPoolConfigEntities);
    }

    @Override
    public void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity) {
        String cacheKey = RegistryEnumVO.THREAD_POOL_CONFIG_PARAMETER_LIST_KEY.getKey().replaceFirst("%s", threadPoolConfigEntity.getAppName()).replaceFirst("%s", threadPoolConfigEntity.getThreadPoolName());

        RBucket<Object> bucket = redissonClient.getBucket(cacheKey);
        //Redisson通过bucket操作数据
        //存放单个线程池的信息，并且存放30天
        bucket.set(threadPoolConfigEntity, Duration.ofDays(30));
    }
}
