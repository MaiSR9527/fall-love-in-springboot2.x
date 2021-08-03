package com.msr.better.config;

import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-03 22:13:35
 */
@Configuration
public class EncryptConfig {
//    @Bean
//    @Conditional(MessageEncryptCondition.class)
//    public MessageEncryptBean messageEncryptBean() {
//        return new MessageEncryptBean();
//    }

    static class MessageEncryptCondition implements Condition {
        @Override
        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
            Resource resource = conditionContext.getResourceLoader().getResource("message.txt");
            Environment environment = conditionContext.getEnvironment();
            return resource.exists() && environment.containsProperty("message.encrypt.enable");
        }
    }
}

