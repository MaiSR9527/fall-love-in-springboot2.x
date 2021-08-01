package com.msr.better.jdbc.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-01 18:19:07
 */
@Configuration
@Slf4j
public class DatasourceType implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        DataSource dataSource = context.getBean(DataSource.class);
        log.info("----------------------------------------------------");
        log.info("using datasource {}", dataSource.getClass().getName());
        log.info("----------------------------------------------------");
    }
}
