package com.msr.better.quick.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @date: 2023-10-24
 * @author: maisrcn@qq.com
 */
@ImportResource("classpath:provider.xml")
@SpringBootApplication
public class ProviderXmlApp {
    private static final Logger logger = LoggerFactory.getLogger(ProviderXmlApp.class);

    public static void main(String[] args) {
        SpringApplication.run(ProviderXmlApp.class, args);
        logger.info("xml provider start success");
    }
}
