package com.msr.better.mq;

import com.msr.better.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 23:20:30
 */
@Component
public class MessageMonitor {
    @Autowired
    private RedisUtil redisUtil;

    public MessageMonitor() {
    }

    public int getMessageLeft(String messageType) {
        return this.redisUtil.llen(messageType).intValue();
    }
}

