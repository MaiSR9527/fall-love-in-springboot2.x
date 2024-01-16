package com.msr.better.bus.test;

import com.msr.better.common.event.UserCreateEvent;
import com.msr.better.common.event.UserDeleteEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date: 2024-01-16
 * @author: maisr@tsintergy.com
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("publish/create")
    public Object publishCreate() {
        UserCreateEvent userCreateEvent = new UserCreateEvent();
        userCreateEvent.setUserId("1");
        userCreateEvent.setUsername("Lil Hua");
        userCreateEvent.setAge(18);
        userCreateEvent.setGender("Male");
        applicationContext.publishEvent(userCreateEvent);
        return "success";
    }

    @GetMapping("publish/delete")
    public Object publishDelete() {
        UserDeleteEvent userDeleteEvent = new UserDeleteEvent();
        userDeleteEvent.setUserId("1");
        applicationContext.publishEvent(userDeleteEvent);
        return "success";
    }
}
