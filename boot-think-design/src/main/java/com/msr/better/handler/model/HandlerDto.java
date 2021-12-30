package com.msr.better.handler.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021/12/28
 */
@Data
public class HandlerDto implements Serializable {

    private static final long serialVersionUID = -7026268333158630263L;

    /**
     * 执行Id
     */
    private Long executeId;

    private Integer operateType;

    private Long userId;

    private String executeResult;

    private Integer executeStatus;

    private String executeDescription;

    private Date startTime;

    private Date endTime;

    private String creator;

    private Date createTime;

    private String modifier;

    private Date updateTime;


}
