package com.msr.better.redis.controller;

import com.msr.better.redis.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Set;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-30 00:09
 **/
@RestController
@RequestMapping("test")
public class TestRedisController {

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("add")
    public void add(@RequestParam("key") String key, @RequestParam("value") String value) {
        redisUtil.set(key, value);
    }

    @GetMapping("list")
    public Object listAllKeys() {
        Set<String> keys = redisUtil.keys("*");
        HashMap<String, Object> map = new HashMap<>();
        for (String key : keys) {
            String value = redisUtil.get(key);
            map.put(key, value);
        }
        return map;
    }
}
