package com.msr.better.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 20:14:35
 */
@Data
public class Result implements Serializable {

    private int code;
    private String msg;
    private Object data;

}
