package com.msr.better.mvc.config;

import com.msr.better.mvc.controller.push.BaiDuPush;
import com.msr.better.mvc.entity.BaiDuPushOk;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022-04-27
 **/
@Configuration
public class PushConfig {
    @Bean
    public BaiDuPush<BaiDuPushOk> baiDuPush() {
        return new BaiDuPush<>();
    }
}
