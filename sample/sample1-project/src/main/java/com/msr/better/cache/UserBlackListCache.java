package com.msr.better.cache;

import com.msr.better.constant.CommonConstant;
import com.msr.better.util.RedisUtil;
import org.springframework.stereotype.Component;

/**
 * 黑名单缓存
 *
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 23:30:29
 */
@Component
public class UserBlackListCache {

    private final RedisUtil redisUtil;

    public UserBlackListCache(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    /**
     * 增加进入黑名单
     *
     * @param mobile 标识
     */
    public void addInto(String mobile) {
        redisUtil.hset(CommonConstant.RedisKey.USER_BLACK_LIST, mobile, "");
    }

    /**
     * 是否在黑名单中
     *
     * @param mobile 标识
     * @return bool
     */
    public boolean isIn(String mobile) {
        return redisUtil.hget(CommonConstant.RedisKey.USER_BLACK_LIST, mobile, String.class) != null;
    }
}
