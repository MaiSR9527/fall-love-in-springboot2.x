package com.msr.better.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-07-31 17:13:51
 */
@RestController
public class HelloController {

    @GetMapping("hello")
    public List<String> hello() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            list.add("hello:" + i);
        }
        return list;
    }
}
