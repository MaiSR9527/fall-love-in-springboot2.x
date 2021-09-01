package com.msr.better.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import wang.moshu.message.Message;
import wang.moshu.message.RedisUtil;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 23:16:13
 */
@Component
public class MessageTrunk {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    @Qualifier("messageTrunkTaskExecutor")
    private ThreadPoolTaskExecutor threadPool;
    private int failRetryTimes = 3;
    private int threadPoolFullSleepSeconds = 1;

    public MessageTrunk() {
    }

    public ThreadPoolTaskExecutor getThreadPool() {
        return this.threadPool;
    }

    public void setThreadPool(ThreadPoolTaskExecutor threadPool) {
        this.threadPool = threadPool;
    }

    public int getFailRetryTimes() {
        return this.failRetryTimes;
    }

    public void setFailRetryTimes(int failRetryTimes) {
        this.failRetryTimes = failRetryTimes;
    }

    public int getThreadPoolFullSleepSeconds() {
        return this.threadPoolFullSleepSeconds;
    }

    public void setThreadPoolFullSleepSeconds(int threadPoolFullSleepSeconds) {
        this.threadPoolFullSleepSeconds = threadPoolFullSleepSeconds;
    }

    public void put(Message message) {
        this.redisUtil.rpush(message.getKey().toString(), message, 0);
    }
}
