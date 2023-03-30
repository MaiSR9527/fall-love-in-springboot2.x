package com.msr.better.config;

import com.msr.better.listener.SyncRequestPathListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author maisr@tsintergy.com
 * @change
 * @date 2023-03-30 11:58:57
 */
@Configuration
public class ShenYuConfig {

    @Bean
    public SyncRequestPathListener syncRequestPathListener() {
        return new SyncRequestPathListener();
    }
}
