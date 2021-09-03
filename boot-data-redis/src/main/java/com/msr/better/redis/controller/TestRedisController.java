package com.msr.better.redis.controller;

import com.msr.better.redis.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

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

    @GetMapping("/enqueue")
    public Object enqueue() {
        for (int i = 0; i < 10; i++) {
            redisUtil.rpush("list-queue", "value-" + i);
        }
        return "success";
    }

    @GetMapping("/dequeue")
    public Object dequeue() {
        System.out.println(redisTemplate.opsForList().size("list-queue"));
        for (int i = 0; i < 10; i++) {
            System.out.println(redisUtil.lpop("list-queue"));
        }
        return "success";
    }

    @GetMapping("enstack")
    public Object enstack() {
        for (int i = 0; i < 10; i++) {
            redisUtil.rpush("list-stack", "value-" + i);
        }
        return "success";
    }

    @GetMapping("destack")
    public Object destack() {
        while (true) {
            String rpop = redisUtil.rpop("list-stack");
            System.out.println(rpop);
            if (rpop == null) {
                break;
            }
        }
        return "success";
    }


}
