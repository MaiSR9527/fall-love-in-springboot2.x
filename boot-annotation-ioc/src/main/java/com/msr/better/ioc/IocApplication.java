package com.msr.better.ioc;

import com.msr.better.ioc.model.BusinessPerson;
import com.msr.better.ioc.model.User;
import com.msr.better.ioc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-28 23:24
 **/
@Slf4j
@SpringBootApplication
//@ComponentScan(excludeFilters = {@ComponentScan.Filter(
//        type = FilterType.ANNOTATION,
//        classes = {Service.class}
//)})
public class IocApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(IocApplication.class, args);
        BusinessPerson businessPerson = applicationContext.getBean(BusinessPerson.class);
        businessPerson.service();
    }

    private static void test(ConfigurableApplicationContext applicationContext) {
        User user = (User) applicationContext.getBean("user");
        UserService userService = applicationContext.getBean(UserService.class);
        log.info("bean:{}", userService.hashCode());
        log.info("bean:{}", user.toString());
    }

    @Bean(name = "dataSource")
    public DataSource dataSource() {

        Properties props = new Properties();
        props.setProperty("driver", "com.mysql.cj.jdbc.Driver");
        props.setProperty("url", "jdbc:mysql://localhost:3306/db");
        props.setProperty("username", "root");
        props.setProperty("password", "123456");
        DataSource dataSource = null;
        try {
            dataSource = BasicDataSourceFactory.createDataSource(props);
        } catch (Exception e) {
            e.printStackTrace();
            return dataSource;
        }
        return dataSource;
    }
}
