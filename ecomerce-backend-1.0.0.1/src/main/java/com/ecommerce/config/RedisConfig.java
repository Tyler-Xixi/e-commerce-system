package com.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis 配置类
 * 
 * 配置 RedisTemplate 的序列化策略，确保：
 * 1. Key 使用 String 序列化，保证可读性
 * 2. Value 使用 JSON 序列化，支持复杂对象存储
 * 3. Hash 结构的 Key 和 Value 也采用相同策略
 */
@Configuration
public class RedisConfig {

    /**
     * 配置 RedisTemplate Bean
     * 
     * RedisTemplate 是 Spring Data Redis 提供的核心操作类，用于与 Redis 进行交互。
     * 通过自定义序列化器，解决默认序列化器（JdkSerializationRedisSerializer）存在的问题：
     * - 默认序列化器会在数据前添加类型前缀，导致 Redis 中数据可读性差
     * - 默认序列化器不支持复杂对象的序列化和反序列化
     * 
     * @param factory Redis 连接工厂，由 Spring 自动注入
     * @return 配置好的 RedisTemplate 实例
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置 Redis 连接工厂
        template.setConnectionFactory(factory);
        // Key 序列化器：使用 String 序列化
        template.setKeySerializer(new StringRedisSerializer());
        // Value 序列化器：使用 JSON 序列化，支持复杂对象
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        // Hash Key 序列化器：使用 String 序列化
        template.setHashKeySerializer(new StringRedisSerializer());
        // Hash Value 序列化器：使用 JSON 序列化
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        // 初始化模板配置（必须调用，否则配置不生效）
        template.afterPropertiesSet();
        return template;
    }
}