package com.msr.better.quick.xml.impl;

import com.msr.better.quick.xml.api.HelloFacadeService;

/**
 * @date: 2023-10-24
 * @author: maisrcn@qq.com
 */
public class HelloFacadeServiceImpl implements HelloFacadeService {
    @Override
    public String sayHello() {
        return "hi friend!";
    }
}
