package com.msr.better.domain;

import lombok.Data;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-02 00:27:03
 */
@Data
public class SysUser {
    private Long id;
    private Integer age;
    private Integer gender;
    private String username;
    private String email;
    private String mobile;
}
