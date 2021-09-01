package com.msr.better.cache;

import com.msr.better.cache.base.CacheWorker;
import com.msr.better.constant.CommonConstant;
import com.msr.better.dao.GoodsMapper;
import com.msr.better.domain.Goods;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;

/**
 * 获取商品信息缓存工作器
 *
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 23:29:10
 */
@Component
public class GoodsInfoCacheWorker extends CacheWorker<Integer, Goods> {

    @Resource
    private GoodsMapper goodsMapper;

    @Override
    protected Goods getDataWhenNoCache(Integer goodsId) {
        return goodsMapper.selectByPrimaryKey(goodsId);
    }

    @Override
    protected String getKey(Integer goodsId) {
        return MessageFormat.format(CommonConstant.RedisKey.GOODS_INFO_BY_ID, goodsId);
    }

    @Override
    protected int getExpireSeconds() {
        return CommonConstant.RedisKeyExpireSeconds.GOODS_STORE_BY_ID;
    }

}
