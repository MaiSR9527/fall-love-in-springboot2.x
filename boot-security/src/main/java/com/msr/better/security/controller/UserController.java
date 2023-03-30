package com.msr.better.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: maisrcn@qq.com
 * @change:
 * @date: 2023-03-22 14:32:02
 */
@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("/getUser")
    public Object getUser() {
        return "get user successfully";
    }
}
