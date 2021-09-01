package com.msr.better.cache;

import com.msr.better.constant.CommonConstant;
import com.msr.better.domain.Goods;
import com.msr.better.util.RedisUtil;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * 商品redis库存量
 *
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 23:29:27
 */
@Component
public class GoodsRedisStoreCache {

    private final RedisUtil redisUtil;

    public GoodsRedisStoreCache(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    public void doInit(Goods goods) {
        String key = getKey(goods.getRandomName());
        // 如果没有初始化库存则初始化
        if (!redisUtil.exists(key)) {
            // TODO 这里考虑可以把redis库存设定大一点，这样即使用户占位成功，下单也要及时
            redisUtil.set(key, goods.getStore());
        }

    }

    private String getKey(String goodsRandomName) {
        return MessageFormat.format(CommonConstant.RedisKey.REDIS_GOODS_STORE, goodsRandomName);
    }

    /**
     * 减redis库存
     *
     * @param goodsRandomName 商品名
     * @return bool
     */
    public boolean decrStore(String goodsRandomName) {
        String key = getKey(goodsRandomName);
        // 减redis库存
        if (redisUtil.decr(key) >= 0) {
            // 如果减成功
            return true;
        } else {
            redisUtil.incr(key);
            return false;
        }
    }

    /**
     * 加redis库存
     *
     * @param goodsRandomName 商品名
     */
    public void incrStore(String goodsRandomName) {
        redisUtil.incr(getKey(goodsRandomName));
    }
}
