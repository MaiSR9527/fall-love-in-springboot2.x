package com.msr.better.redis.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PrivateNotice implements Serializable {

    private static final long serialVersionUID = -2242616125149750670L;

    private Long id;
    private Long fromUserId;
    private Long toUserId;
    private String content;
    private Date createTime;
    private Date updateTime;

}
