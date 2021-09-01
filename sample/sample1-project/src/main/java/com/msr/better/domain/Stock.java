package com.msr.better.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 20:32:59
 */
@Data
public class Stock implements Serializable {
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 库存
     */
    private Integer count;

    /**
     * 已售
     */
    private Integer sale;

    /**
     * 乐观锁，版本号
     */
    private Integer version;

    private static final long serialVersionUID = 1L;
}
