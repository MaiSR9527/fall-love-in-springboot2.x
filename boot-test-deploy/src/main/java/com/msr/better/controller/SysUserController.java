package com.msr.better.controller;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-04 00:32:50
 */
@RestController
public class SysUserController {

    @GetMapping("/hello/{id}")
    public Object findById(@PathVariable Long id, @RequestParam String name) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        return map;
    }
}
