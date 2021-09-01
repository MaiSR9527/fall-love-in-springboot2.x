package com.msr.better.constant;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 20:58:27
 */
public class CommonConstant {
    public interface RedisKey {
        String GOODS_STORE_BY_ID = "GOODS_STORE_BY_GOODS_ID_{0}";

        String GOODS_INFO_BY_ID = "GOODS_INFO_BY_GOODS_ID_{0}";

        // IP黑名单
        String IP_BLACK_LIST = "IP_BLACK_LIST";

        // 用户黑名单
        String USER_BLACK_LIST = "USER_BLACK_LIST";

        // 秒杀处理列表
        String SPIKE_HANDLE_LIST = "SPIKE_HANDLE_LIST_GOODS_RANDOM_NAME:{0}";

        // redis库存
        String REDIS_GOODS_STORE = "REDIS_GOODS_STORE_GOODS_RANDOM_NAME:{0}";

        // redis占位成功下单token
        String SPIKE_SUCCESS_TOKEN = "SPIKE_SUCCESS_TOKEN_MOBILE:{0}_GOODS_RANDOM_NAME:{1}_";

        // redis占位成功下单token前缀
        String SPIKE_SUCCESS_TOKEN_PREFIX = "SPIKE_SUCCESS_TOKEN_MOBILE";

        // 秒杀结束标志
        String SPIKE_FINISH_FLAG = "SPIKE_FINISH_FLAG_GOODS_RANDOM_NAME:{0}";

    }

    public interface RedisKeyExpireSeconds {
        int GOODS_STORE_BY_ID = 3 * 24 * 60 * 60;
    }

    /**
     * 限流倍数
     *
     * @author MaiShuRen
     * @site https://www.maishuren.top
     * @since 2021-09-01 20:58:27
     */
    public interface CurrentLimitMultiple {
        // 商品购买限流倍数
        int GOODS_BUY = 1;
    }

    /**
     * cookie id
     */
    public static final String COOKIE_NAME = "SPIKE_ID";

    /**
     * token有效期，单位：毫秒
     */
    public static final long TOKEN_EFECTIVE_MILLISECONDS = 3 * 60 * 1000;
}
