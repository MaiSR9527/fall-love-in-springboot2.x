package com.msr.better.redis.stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MaiShuRen
 * @site <a href="https://www.maishuren.top">maiBlog</a>
 * @since 2023-01-14 10:33
 **/
@Component
public class StreamTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void pub() {
        StreamOperations<String, Object, Object> opsForStream = redisTemplate.opsForStream();
        Map<String, String> map = new HashMap<>();
        map.put("k1", "v1");
        opsForStream.add("st-prod", map);
    }

    public void sub() {
        StreamOperations<String, Object, Object> opsForStream = redisTemplate.opsForStream();
    }
}
