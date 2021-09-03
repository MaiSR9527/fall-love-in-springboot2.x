package com.msr.better.cache;

import com.msr.better.constant.CommonConstant;
import com.msr.better.util.RedisUtil;
import org.springframework.stereotype.Component;

/**
 * 秒杀正在处理请求列表
 *
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 23:29:59
 */
@Component
public class SpikeHandlingListCache {

    private final RedisUtil redisUtil;

    public SpikeHandlingListCache(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 增加到处理列表
     *
     * @param mobile          用户标识
     * @param goodsRandomName 商品标识
     */
    public void add2HanleList(String mobile, String goodsRandomName) {
        redisUtil.hset(getKey(goodsRandomName), mobile, mobile);
    }

    /**
     * 增加到处理列表
     *
     * @param mobile          用户标识
     * @param goodsRandomName 商品标识
     */
    public void removeFromHanleList(String mobile, String goodsRandomName) {
        redisUtil.hdel(getKey(goodsRandomName), mobile);
    }

    /**
     * 是否在正在处理列表中
     *
     * @param mobile          用户标识
     * @param goodsRandomName 商品标识
     */
    public boolean isInHandleList(String mobile, String goodsRandomName) {
        return redisUtil.hget(getKey(goodsRandomName), mobile, String.class) != null;
    }

    private String getKey(String goodsRandomName) {
        return CommonConstant.RedisKey.SPIKE_HANDLE_LIST + goodsRandomName;
    }
}
