package com.msr.better.security.config;

import com.msr.better.security.configurer.CustomLoginConfigurer;
import com.msr.better.security.filter.CustomFilter1;
import com.msr.better.security.filter.CustomFilter2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: maisrcn@qq.com
 * @change:
 * @date: 2023-03-22 14:00:48
 */
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/ignore");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        List<Header> headerList = new ArrayList<>();
        //access-control-expose-headers: Authorization是指允许在请求头里存放token,后端通过请求头来获取前端传来的token。
        headerList.add(new Header("Access-Control-Expose-Headers", "Authorization"));
        HeaderWriter headerWriter = new StaticHeadersWriter(headerList);




        http.csrf().disable()
                //.formLogin()
                //.disable()
                .cors()
                .and()

                .headers()
                .addHeaderWriter(headerWriter)
                .and()

                .apply(new CustomLoginConfigurer<>())
                .and()

                .logout()
                .logoutUrl("/logout");

    }
}
