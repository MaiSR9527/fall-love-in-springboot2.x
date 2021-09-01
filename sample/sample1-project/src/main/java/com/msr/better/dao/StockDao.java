package com.msr.better.dao;

import com.msr.better.domain.Stock;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 20:35:36
 */
public interface StockDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Stock record);

    int insertSelective(Stock record);

    Stock selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Stock record);

    int updateByPrimaryKey(Stock record);
}
