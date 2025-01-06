package com.bicycle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {

    @Bean
    public StringRedisTemplate getRedisTemplate(RedisConnectionFactory factory){
        //RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        //redisTemplate.setConnectionFactory(factory);
        return new StringRedisTemplate(factory);
    }
}
