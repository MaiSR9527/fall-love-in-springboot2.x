package com.msr.better.cache.base;

import com.msr.better.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * redis限流器
 *
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 22:10:00
 */
public abstract class CurrentLimiter<P> {
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 做限流，如果超过了流量则抛出异常
     *
     * @param id
     * @param errorMsg
     * @category @author xiangyong.ding@weimob.com
     * @since 2017年3月16日 下午1:51:38
     */
    public void doLimit(P param, String errorMsg) {
        // 获取流量最大值
        int limit = getLimit(param);

        // 现有流量值
        Integer currentLimit = getCurrentLimit();

        // 如果现有流量值大于了限流值，或者自增了流量之后大于了限流值则表示操作收到了限流
        if (currentLimit != null && currentLimit >= limit) {
            throw new RuntimeException(errorMsg);
        }
    }

    /**
     * 获取即时流量值
     */
    protected abstract int getCurrentLimit();

    /**
     * 获取限流器名字
     *
     * @return
     * @category @author xiangyong.ding@weimob.com
     * @since 2017年3月16日 下午1:38:24
     */
    protected abstract String getLimiterName(P param);

    /**
     * 获取限流的最大流量
     *
     * @return
     * @category @author xiangyong.ding@weimob.com
     * @since 2017年3月16日 下午1:39:17
     */
    protected abstract int getLimit(P param);

}
