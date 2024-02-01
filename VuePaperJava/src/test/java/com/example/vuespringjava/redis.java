package com.example.vuespringjava;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.File;
import java.io.IOException;

@SpringBootTest
@DisplayName("redis的测试类")
public class redis {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    void redisverification(){
        ValueOperations<String, String> redisTmp = redisTemplate.opsForValue();
            redisTmp.set("hello","word");
        String s = redisTmp.get("hello");
        System.out.println(s);

    }
    @Test
    void  file() throws IOException {
        String path="upload/";
        String lastName="test.txt";
        File file = new File(path+lastName);
        if (!file.exists()) {
            file.createNewFile();
//                file.mkdirs();
        }
        System.out.println(file);
    }




}
