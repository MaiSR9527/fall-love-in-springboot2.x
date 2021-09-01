package com.msr.better;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-21 11:09:43
 */
@ComponentScan(basePackages = {"com.msr.better"})
@SpringBootApplication
public class ProApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProApplication.class, args);
    }
}
