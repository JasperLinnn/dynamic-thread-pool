package com.jasper.middleware.dynamic.thread.pool.sdk.registry;

import com.jasper.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * @author zihong
 * @description 注册中心接口
 * @create 2025/4/1 17:46
 **/
public interface IRegistry {

    /**
     * 注册多个线程池配置
     *
     * @param threadPoolConfigEntities 线程池配置列表
     */
    void reportThreadPool(List<ThreadPoolConfigEntity> threadPoolConfigEntities);

    /**
     * 注册单个线程池配置参数
     *
     * @param threadPoolConfigEntity 线程池配置参数
     */
    void reportThreadPoolConfigParameter(ThreadPoolConfigEntity threadPoolConfigEntity);
}
