package com.jasper.middleware.dynamic.thread.pool.trigger;

import com.alibaba.fastjson2.JSON;
import com.jasper.middleware.dynamic.thread.pool.sdk.domain.model.entity.ThreadPoolConfigEntity;
import com.jasper.middleware.dynamic.thread.pool.sdk.domain.model.vo.RegistryEnumVO;
import com.jasper.middleware.dynamic.thread.pool.types.Response;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zihong
 * @description
 * @create 2025/4/8 16:42
 **/
@RestController
@RequestMapping("/api/v1/dynamic/thread/pool")
@CrossOrigin("*")
@Slf4j
public class DynamicThreadPoolController {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 查询线程池列表
     */
    @GetMapping("query_thread_pool_list")
    public Response<List<ThreadPoolConfigEntity>> queryThreadPoolList() {
        RList<ThreadPoolConfigEntity> list = redissonClient.getList(RegistryEnumVO.THREAD_POOL_CONFIG_LIST_KEY.getKey());
        return Response.success(list.readAll());
    }

    /**
     * 查询单个的线程池参数
     *
     * @param appName        项目名
     * @param threadPoolName 线程池的名称
     * @return
     */
    @GetMapping("/query_thread_pool_config")
    public Response<ThreadPoolConfigEntity> queryThreadPoolConfig(@RequestParam String appName,
                                                                  @RequestParam String threadPoolName) {
        try {
            String key = String.format(RegistryEnumVO.THREAD_POOL_CONFIG_PARAMETER_LIST_KEY.getKey(), appName, threadPoolName);
            RBucket<Object> bucket = redissonClient.getBucket(key);
            return Response.success((ThreadPoolConfigEntity) bucket.get());
        } catch (Exception e) {
            log.error("查询线程池参数异常", e);
            return Response.error();
        }
    }

    /**
     * 修改线程池的配置
     *
     * @param request
     * @return
     */
    @PostMapping("/update_thread_pool_config")
    public Response<Boolean> updateThreadPoolConfig(@RequestBody ThreadPoolConfigEntity request){
        try{
            log.info("修改线程池配置开始 {} {} {}", request.getAppName(), request.getThreadPoolName(), JSON.toJSONString(request));
            String topicName = String.format(RegistryEnumVO.DYNAMIC_THREAD_POOL_REDIS_TOPIC.getKey(), request.getAppName());
            RTopic topic = redissonClient.getTopic(topicName);
            topic.publish(request);
            log.info("修改线程池配置完成 {} {}", request.getAppName(), request.getThreadPoolName());
            return Response.success(true);
        }catch (Exception e){
            log.error("修改线程池的配置的方法异常: {}",JSON.toJSONString(request),e);
            return Response.error(false);
        }
    }
}
