package com.example.vuespringjava.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){

        RedisTemplate<Object, Object> RedisTemplate = new RedisTemplate<>();
        RedisTemplate.setConnectionFactory(redisConnectionFactory);


        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(new ObjectMapper());

        RedisTemplate.setKeySerializer(new StringRedisSerializer());
        RedisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        RedisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
        RedisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        return RedisTemplate;
    }

}
