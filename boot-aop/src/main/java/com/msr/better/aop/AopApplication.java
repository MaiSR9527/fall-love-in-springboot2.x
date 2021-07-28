package com.msr.better.aop;

import com.msr.better.aop.apsect.MyAspect1;
import com.msr.better.aop.apsect.MyAspect2;
import com.msr.better.aop.apsect.MyAspect3;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-30 00:00
 **/
@SpringBootApplication
public class AopApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopApplication.class, args);
    }

    @Bean
    public MyAspect3 myAspect3() {
        return new MyAspect3();
    }

    @Bean
    public MyAspect1 myAspect1() {
        return new MyAspect1();
    }

    @Bean
    public MyAspect2 myAspect2() {
        return new MyAspect2();
    }


}
