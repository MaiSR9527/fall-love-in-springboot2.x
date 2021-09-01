package com.msr.better.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 20:15:57
 */
@Data
public class QueryParam implements Serializable {
    private int pageNum;
    private int pageSize;
}
