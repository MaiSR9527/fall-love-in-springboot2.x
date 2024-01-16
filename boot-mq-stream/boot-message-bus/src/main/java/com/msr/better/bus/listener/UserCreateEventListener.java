package com.msr.better.bus.listener;

import com.msr.better.common.event.UserCreateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @date: 2024-01-16
 * @author: maisrcn@qq.com
 */
@Slf4j
@Component
public class UserCreateEventListener {

    @EventListener
    public void listenerUserCreate(UserCreateEvent userCreateEvent) {
        log.info("receive create user: {}", userCreateEvent);
    }
}
