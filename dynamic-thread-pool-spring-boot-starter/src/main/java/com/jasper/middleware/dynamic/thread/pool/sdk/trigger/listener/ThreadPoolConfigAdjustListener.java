package com.jasper.middleware.dynamic.thread.pool.sdk.trigger.listener;

import com.jasper.middleware.dynamic.thread.pool.sdk.domain.IDynamicThreadPoolService;
import com.jasper.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.jasper.middleware.dynamic.thread.pool.sdk.registry.IRegistry;
import org.redisson.api.listener.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author zihong
 * @description 动态线程池变更监听
 * @create 2025/4/2 15:35
 **/
public class ThreadPoolConfigAdjustListener implements MessageListener<ThreadPoolConfigEntity> {

    private Logger logger = LoggerFactory.getLogger(ThreadPoolConfigAdjustListener.class);

    private final IDynamicThreadPoolService dynamicThreadPoolService;
    private final IRegistry registry;

    public ThreadPoolConfigAdjustListener(IDynamicThreadPoolService dynamicThreadPoolService, IRegistry registry) {
        this.dynamicThreadPoolService = dynamicThreadPoolService;
        this.registry = registry;
    }


    /**
     * 当接收到消息时，更新并上报线程池配置
     * 此方法在接收到新的线程池配置信息时被调用，负责更新线程池配置并报告更新后的配置情况
     * @param threadPoolConfigEntity 新的线程池配置实体，包含需要更新的线程池配置信息
     */
    @Override
    public void onMessage(CharSequence charSequence, ThreadPoolConfigEntity threadPoolConfigEntity) {
        // 更新线程池配置
        dynamicThreadPoolService.updateThreadPoolConfig(threadPoolConfigEntity);

        // 查询所有线程池配置列表，为后续报告做准备
        List<ThreadPoolConfigEntity> threadPoolConfigEntities = dynamicThreadPoolService.queryThreadPoolList();

        // 上报所有线程池的配置情况
        registry.reportThreadPool(threadPoolConfigEntities);

        // 根据线程池名称查询具体的线程池配置信息
        ThreadPoolConfigEntity threadPoolConfigEntityCurrent = dynamicThreadPoolService.queryThreadPoolConfigByName(threadPoolConfigEntity.getThreadPoolName());

        // 上报特定线程池的详细配置参数
        registry.reportThreadPoolConfigParameter(threadPoolConfigEntityCurrent);
    }
}
