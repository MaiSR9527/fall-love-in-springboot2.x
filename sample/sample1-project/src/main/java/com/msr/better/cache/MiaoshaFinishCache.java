package com.msr.better.cache;

import com.msr.better.constant.CommonConstant;
import com.msr.better.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * 秒杀结束缓存
 *
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 23:29:42
 */
@Component
public class MiaoshaFinishCache {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 设定是否秒杀结束
     *
     * @param goodsId
     * @category @author xiangyong.ding@weimob.com
     * @since 2017年4月18日 下午10:26:27
     */
    public void setFinish(String goodsRandomName) {
        redisUtil.set(getKey(goodsRandomName), "");
    }

    /**
     * 指定商品秒杀是否结束
     *
     * @param goodsId
     * @return bool
     */
    public boolean isFinish(String goodsRandomName) {
        return redisUtil.get(getKey(goodsRandomName), String.class) != null;
    }

    private String getKey(String goodsRandomName) {
        return MessageFormat.format(CommonConstant.RedisKey.SPIKE_FINISH_FLAG, goodsRandomName);
    }
}
