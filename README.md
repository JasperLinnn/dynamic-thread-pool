# 动态线程池项目说明

## 项目概述

动态线程池（Dynamic Thread Pool）是一个基于 Spring Boot 的线程池动态调整框架，允许在应用运行时动态调整线程池参数，无需重启应用。该项目通过 Redis 作为注册中心和消息传递媒介，实现了线程池配置的集中管理和实时调整。

## 项目结构

项目由三个主要模块组成：

- **dynamic-thread-pool-spring-boot-starter**：核心组件，提供动态线程池的基础功能
- **dynamic-thread-pool-admin**：管理控制台，用于查看和调整线程池配置
- **dynamic-thread-pool-test**：示例应用，展示如何使用动态线程池

## 技术栈

- Spring Boot 2.7.12
- Redisson 3.26.1（Redis 客户端）
- Lombok
- FastJSON2
- JUnit

## 核心功能

1. **线程池动态调整**：运行时调整核心线程数、最大线程数、队列容量等参数
2. **线程池监控**：实时查看线程池运行状态
3. **配置持久化**：线程池配置存储在 Redis 中，支持跨重启保持配置
4. **分布式支持**：通过 Redis 实现多实例间的配置同步

## 快速开始

### 1. 添加依赖

在您的 Spring Boot 项目中添加以下依赖：

```xml
<dependency>
    <groupId>com.jasper</groupId>
    <artifactId>dynamic-thread-pool-spring-boot-starter</artifactId>
    <version>1.0</version>
</dependency>
```

### 2. 配置 Redis

在 `application.yml` 中添加 Redis 配置：

```yaml
spring:
  application:
    name: your-application-name
  profiles:
    active: dev

# Redis 配置
redis:
  sdk:
    config:
      host: ${your.redis.host}
      port: ${your.redis.port}
      pool-size: 10
      min-idle-size: 5
      idle-timeout: 10000
      connect-timeout: 10000
      retry-attempts: 3
      retry-interval: 1500
      ping-interval: 1000
      keep-alive: true
```

### 3. 配置线程池

创建线程池配置类：

```java
@Data
@ConfigurationProperties(prefix = "thread.pool.executor.config")
public class ThreadPoolConfigProperties {
    /**
     * 核心线程数
     */
    private Integer corePoolSize;
    /**
     * 最大线程数
     */
    private Integer maxPoolSize;
    /**
     * 空闲线程的存活时间
     */
    private Long keepAliveTime;
    /**
     * 队列容量
     */
    private Integer blockQueueSize;
    /**
     * 拒绝策略
     */
    private String policy = "AbortPolicy";
}
```

## 管理控制台

管理控制台提供以下功能：

1. 查看所有线程池列表
2. 查看单个线程池详细配置
3. 动态调整线程池参数

### API 接口

- `GET /api/v1/dynamic/thread/pool/query_thread_pool_list`：查询线程池列表
- `GET /api/v1/dynamic/thread/pool/query_thread_pool_config`：查询单个线程池配置
- `POST /api/v1/dynamic/thread/pool/update_thread_pool_config`：更新线程池配置

## 工作原理

1. 应用启动时，线程池配置注册到 Redis
2. 管理控制台通过 Redis 获取线程池配置信息
3. 当配置变更时，通过 Redis 的发布/订阅机制通知应用
4. 应用接收到配置变更通知后，动态调整线程池参数

## 注意事项

- 确保 Redis 服务可用，否则将影响动态调整功能
- 线程池参数调整需谨慎，避免影响应用性能和稳定性

## 贡献指南

欢迎提交 Issue 和 Pull Request 来完善项目。

## 许可证

本项目采用 MIT 许可证。