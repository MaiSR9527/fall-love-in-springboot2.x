package com.msr.better.ioc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-28 23:24
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Component("user")
public class User {

    @Value("1")
    private Long id;
    @Value("haha")
    private String username;
    @Value("123456")
    private String password;
}
