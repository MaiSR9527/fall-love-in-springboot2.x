package com.msr.better.redis.common;

import lombok.Getter;

@Getter
public enum MessageType {

    PRIVATE_MSG(1, "个人私信"),
    SYSTEM_MSG(2, "系统消息");

    private final Integer eventNum;
    private final String description;

    MessageType(Integer eventNum, String description) {
        this.eventNum = eventNum;
        this.description = description;
    }

}
