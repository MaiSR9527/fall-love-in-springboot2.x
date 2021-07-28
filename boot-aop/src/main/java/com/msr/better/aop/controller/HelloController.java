package com.msr.better.aop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-30 00:00
 **/
@RestController
@RequestMapping("hello")
public class HelloController {

    @GetMapping("test1/{id}")
    public Object test1(@PathVariable("id")Integer id) {
        System.out.println("controller:"+id);
        return "success";
    }

    @GetMapping("test2")
    public Object test2() {
        return "success";
    }
}
