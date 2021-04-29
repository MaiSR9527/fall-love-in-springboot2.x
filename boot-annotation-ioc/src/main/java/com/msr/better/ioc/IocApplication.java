package com.msr.better.ioc;

import com.msr.better.ioc.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-28 23:24
 **/
@Slf4j
@SpringBootApplication
public class IocApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(IocApplication.class, args);
        User user = (User) applicationContext.getBean("user");
       log.info("bean:{}",user.toString());
    }
}
