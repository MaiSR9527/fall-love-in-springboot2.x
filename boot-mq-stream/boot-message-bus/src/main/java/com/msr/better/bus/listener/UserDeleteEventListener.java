package com.msr.better.bus.listener;

import com.msr.better.common.event.UserDeleteEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @date: 2024-01-16
 * @author: maisrcn@qq.com
 */
@Slf4j
@Component
public class UserDeleteEventListener {

    @EventListener
    public void listenerUserCreate(UserDeleteEvent userDeleteEvent) {
        log.info("receive delete userId:{}", userDeleteEvent.getUserId());
    }
}
