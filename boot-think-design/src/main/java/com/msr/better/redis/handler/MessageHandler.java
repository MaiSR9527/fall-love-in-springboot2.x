package com.msr.better.redis.handler;

import com.msr.better.redis.common.MessageType;

import java.util.List;

public interface MessageHandler {
    void handlerMessage();

    List<MessageType> supportedEvent();
}
