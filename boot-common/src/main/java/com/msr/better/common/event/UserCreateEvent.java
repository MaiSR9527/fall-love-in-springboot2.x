package com.msr.better.common.event;

import lombok.Data;

/**
 * @date: 2024-01-16
 * @author: maisrcn@qq.com
 */
@Data
public class UserCreateEvent {

    private String userId;

    private String username;

    private Integer age;

    private String gender;
}
