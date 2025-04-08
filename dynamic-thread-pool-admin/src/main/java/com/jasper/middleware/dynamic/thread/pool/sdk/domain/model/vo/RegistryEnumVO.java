package com.jasper.middleware.dynamic.thread.pool.sdk.domain.model.vo;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author jasper
 * @create 2025/04/01 18:04
 */
public enum RegistryEnumVO {
    THREAD_POOL_CONFIG_LIST_KEY("THREAD_POOL_CONFIG_LIST_KEY", "池化配置列表"),
    // %s_%s = appName_threadPoolName
    THREAD_POOL_CONFIG_PARAMETER_LIST_KEY("THREAD_POOL_CONFIG_PARAMETER_LIST_KEY_%s_%s", "池化配置参数"),
    // %s = appName
    DYNAMIC_THREAD_POOL_REDIS_TOPIC("DYNAMIC_THREAD_POOL_REDIS_TOPIC_%s", "动态线程池监听主题配置");
    ;

    private final String key;
    private final String desc;

    RegistryEnumVO(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    private static Map<String,RegistryEnumVO> cache;

    static {
        cache = Arrays.stream(RegistryEnumVO.values()).collect(Collectors.toMap(RegistryEnumVO::getKey, Function.identity()));
    }

    public static RegistryEnumVO of(String key){
        return cache.get(key);
    }

}