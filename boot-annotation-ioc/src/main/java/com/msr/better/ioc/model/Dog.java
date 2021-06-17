package com.msr.better.ioc.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-28 23:24
 **/
@Slf4j
@Component
@Primary
public class Dog implements Animal {
    @Override
    public void user() {
        log.info("dog is used to Gatekeeper");
    }
}
