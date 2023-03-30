package com.msr.better.security.configurer;

import com.msr.better.security.filter.CustomFilter1;
import com.msr.better.security.filter.CustomFilter2;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * @author: maisrcn@qq.com
 * @change:
 * @date: 2023-03-22 14:08:10
 */
public class CustomLoginConfigurer<T extends CustomLoginConfigurer<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {

    @Override
    public void configure(B http) throws Exception {
        CustomFilter1 customFilter1 = new CustomFilter1();
        CustomFilter2 customFilter2 = new CustomFilter2();
        CustomFilter1 filter1 = postProcess(customFilter1);
        CustomFilter2 filter2 = postProcess(customFilter2);
        http.addFilterBefore(filter1, LogoutFilter.class);
        http.addFilterBefore(filter2, UsernamePasswordAuthenticationFilter.class);
    }
}
