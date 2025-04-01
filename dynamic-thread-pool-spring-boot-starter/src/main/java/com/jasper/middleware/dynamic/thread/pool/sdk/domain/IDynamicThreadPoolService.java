package com.jasper.middleware.dynamic.thread.pool.sdk.domain;

import com.jasper.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;

import java.util.List;

/**
 * @description 动态线程池服务接口
 * @author zihong
 * @create 2025/4/1 16:26
 **/
public interface IDynamicThreadPoolService {

    /**
     * 查询线程池配置列表
     */
    List<ThreadPoolConfigEntity> queryThreadPoolList();

    /**
     * 根据线程池名称查询线程池配置
     * @param threadPoolName 线程池名称
     */
    ThreadPoolConfigEntity queryThreadPoolConfigByName(String threadPoolName);

    /**
     * 更新线程池配置
     * @param threadPoolConfigEntity 线程池配置
     */
    void updateThreadPoolConfig(ThreadPoolConfigEntity threadPoolConfigEntity);

}
