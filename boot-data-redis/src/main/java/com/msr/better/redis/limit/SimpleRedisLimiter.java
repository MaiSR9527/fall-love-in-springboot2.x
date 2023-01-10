package com.msr.better.redis.limit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author MaiShuRen
 * @site <a href="https://www.maishuren.top">maiBlog</a>
 * @since 2023-01-10 00:01
 **/
@Component
public class SimpleRedisLimiter {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean isAllowed(String userId, String key, int period, int maxCount) {

        String redisKey = String.format("hist:%s:%s", userId, key);
        long timestamp = System.currentTimeMillis();
        List<Object> list = redisTemplate.executePipelined((RedisCallback<Long>) connection -> {
            connection.openPipeline();

            connection.multi();
            connection.zAdd(redisKey.getBytes(StandardCharsets.UTF_8), (double) timestamp,
                    String.valueOf(timestamp).getBytes(StandardCharsets.UTF_8));
            connection.zRemRangeByScore(redisKey.getBytes(StandardCharsets.UTF_8), 0, timestamp - period * 1000L);
            Long count = connection.zCard(redisKey.getBytes(StandardCharsets.UTF_8));
            connection.expire(redisKey.getBytes(StandardCharsets.UTF_8), period + 1);
            connection.exec();
            return null;
        }, redisTemplate.getValueSerializer());

        return (Long) list.get(list.size() - 1) <= maxCount;
    }
}
