package com.msr.better.dao;

import com.msr.better.domain.Order;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 21:44:04
 */
public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
}
