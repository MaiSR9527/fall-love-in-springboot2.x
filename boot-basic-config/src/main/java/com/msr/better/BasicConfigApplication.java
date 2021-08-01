package com.msr.better;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-07-31 11:38:00
 */
@SpringBootApplication
public class BasicConfigApplication implements ApplicationListener<WebServerInitializedEvent> {

    private Logger log = LoggerFactory.getLogger(BasicConfigApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BasicConfigApplication.class, args);
    }

    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        log.info("随机启动端口：{}", event.getWebServer().getPort());
    }
}
