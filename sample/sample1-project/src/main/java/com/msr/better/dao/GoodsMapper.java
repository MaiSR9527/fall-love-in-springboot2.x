package com.msr.better.dao;

import com.msr.better.domain.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 21:42:31
 */
public interface GoodsMapper
{
    int deleteByPrimaryKey(Integer id);

    int insert(Goods record);

    int insertSelective(Goods record);

    Goods selectByPrimaryKey(Integer id);

    Goods selectByRandomName(String randomName);

    int updateByPrimaryKeySelective(Goods record);

    int updateByPrimaryKey(Goods record);

    /**
     * 减库存
     *
     * @param goodsId
     * @return
     */
    int reduceStore(@Param("goodsId") Integer goodsId);

    /**
     * 根据主键ID查库存
     *
     * @param id
     * @return
     */
    Integer selectStoreByPrimaryKey(Integer id);

    List<Goods> selectAll();

    /**
     * 减库存+生成订单
     *
     * @param record
     * @return
     */
    void doOrder(Map<String, Object> paramMap);
}
