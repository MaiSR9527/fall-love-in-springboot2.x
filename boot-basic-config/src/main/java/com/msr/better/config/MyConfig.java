package com.msr.better.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.system.JavaVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-03 21:04:04
 */
@ConditionalOnJava(range = ConditionalOnJava.Range.EQUAL_OR_NEWER,value = JavaVersion.EIGHT)
@ConditionalOnProperty(name = "",havingValue = "true",matchIfMissing = true)
@Configuration
@ConditionalOnBean
public class MyConfig {


}
