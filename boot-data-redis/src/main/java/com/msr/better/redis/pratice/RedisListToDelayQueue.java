package com.msr.better.redis.pratice;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.msr.better.redis.util.RedisUtil;

import java.lang.reflect.Type;

/**
 * Redis实现的延时队列
 *
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021/12/28
 */
public class RedisListToDelayQueue<T> {

    private Type taskType = new TypeReference<Task<T>>() {
    }.getType();
    private String queueKey;
    private RedisUtil redisUtil;

    public RedisListToDelayQueue(RedisUtil redisUtil, String queueKey) {
        this.redisUtil = redisUtil;
        this.queueKey = queueKey;
    }

    public void delay(T msg) {
        Task<T> task = new Task<>();
        task.taskId = UUID.fastUUID().toString(true);
        task.msg = msg;
        String s = JSON.toJSONString(task);
    }

    static class Task<T> {
        private String taskId;
        private T msg;
    }
}
