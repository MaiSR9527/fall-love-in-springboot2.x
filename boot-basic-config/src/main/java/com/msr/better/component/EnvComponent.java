package com.msr.better.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-03 19:58:59
 */
@Component
public class EnvComponent {

    @Autowired
    private Environment environment;

    public int getPort() {
        Integer port = environment.getProperty("server.port", Integer.class);
        System.out.println(port);
        return port;
    }
}
