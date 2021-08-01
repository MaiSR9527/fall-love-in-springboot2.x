package com.msr.better.jdbc.domain;

import lombok.Data;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-30 00:00
 **/
@Data
public class Student {

    private Long id;
    private String name;
    private String gender;
    private Integer age;
}
