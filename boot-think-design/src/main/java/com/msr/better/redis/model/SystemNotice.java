package com.msr.better.redis.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SystemNotice implements Serializable {

    private static final long serialVersionUID = -7002626605417919402L;

    private Long id;
    private Long userId;
    private String content;
    private Date createTime;
    private Date updateTime;
}
