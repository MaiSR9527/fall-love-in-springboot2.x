package com.msr.better.controller.order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author maisr@tsintergy.com
 * @change
 * @date 2023-03-30 11:56:02
 */
@RestController
@RequestMapping("order")
public class Order {

    @GetMapping("detail")
    public Object orderDetai(String orderId) {
        Map<String, Object> info = new HashMap<>();
        info.put("orderId", orderId);
        info.put("goodsName", "iPhone 14 pro max");
        info.put("price", 8888);
        return info;
    }
}
