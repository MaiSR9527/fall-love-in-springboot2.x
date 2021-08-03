package com.msr.better.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-03 20:05:51
 */
@Component
public class ValueAutoComponent {

    @Value("${server.port}")
    public Integer port;


}
