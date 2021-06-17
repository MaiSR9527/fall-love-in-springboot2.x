package com.msr.better.ioc.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-28 23:24
 **/
@Slf4j
@Component
public class Cat implements Animal {
    @Override
    public void user() {
        log.info("cat is used to Catch mice");
    }
}
