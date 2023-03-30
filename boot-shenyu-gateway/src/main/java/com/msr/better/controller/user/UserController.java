package com.msr.better.controller.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author maisr@tsintergy.com
 * @change
 * @date 2023-03-30 11:52:31
 */
@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("info")
    public Object userInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("name", "jack");
        info.put("age", 18);
        info.put("gender", "male");
        return info;
    }
}
