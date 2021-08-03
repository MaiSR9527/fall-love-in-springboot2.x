package com.msr.better.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-04 00:32:50
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public Object hello() {
        return null;
    }
}
