package com.msr.better.cache;

import com.msr.better.constant.CommonConstant;
import com.msr.better.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.Set;
import java.util.UUID;

/**
 * 秒杀获取到了下单资格token缓存
 *
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 23:30:13
 */
@Component
public class MiaoshaSuccessTokenCache {

    private final RedisUtil redisUtil;

    private final GoodsRedisStoreCache goodsRedisStoreCache;

    private final MiaoshaHandlingListCache miaoshaHandlingListCache;

    public MiaoshaSuccessTokenCache(MiaoshaHandlingListCache miaoshaHandlingListCache, GoodsRedisStoreCache goodsRedisStoreCache, RedisUtil redisUtil) {
        this.miaoshaHandlingListCache = miaoshaHandlingListCache;
        this.goodsRedisStoreCache = goodsRedisStoreCache;
        this.redisUtil = redisUtil;
    }

    /**
     * 获取随机名称
     *
     * @return uuid
     */
    public String getToken() {
        return UUID.randomUUID().toString();
    }

    public String genToken(String mobile, String goodsRandomName) {
        String key = getKey(mobile, goodsRandomName);
        String token = getToken();
        redisUtil.set(key + token, System.currentTimeMillis());
        // redisUtil.incr("test_ddd");
        return token;
    }

    /**
     * 查询token
     *
     * @param mobile          用户标识
     * @param goodsRandomName 商品标识
     * @return 结果
     */
    public String queryToken(String mobile, String goodsRandomName) {
        Set<String> keys = redisUtil.keys(getKey(mobile, goodsRandomName) + "*");
        if (!CollectionUtils.isEmpty(keys)) {
            String key = keys.iterator().next();

            return key.substring(key.lastIndexOf("_") + 1);
        }
        return StringUtils.EMPTY;
    }

    /**
     * 验证token
     * false:token无效，true:token有效
     *
     * @param token token
     * @return 结果
     */
    public boolean validateToken(String mobile, String goodsRandomName, String token) {
        String key = getKey(mobile, goodsRandomName) + token;
        Long tokenSavedTimeStamp = redisUtil.get(key, Long.class);

        // 判断token是否过了有效期
        if (tokenSavedTimeStamp != null
                && (System.currentTimeMillis() - tokenSavedTimeStamp < CommonConstant.TOKEN_EFECTIVE_MILLISECONDS)) {
            // 已经验证了的清楚掉
            redisUtil.delete(key);
            // 如果token验证成功
            return true;
        } else if (tokenSavedTimeStamp != null) {
            // 失效了的清楚掉
            redisUtil.delete(key);
            // 如果token存在，且是过期的，则回馈redis库存
            goodsRedisStoreCache.incrStore(goodsRandomName);

            miaoshaHandlingListCache.removeFromHanleList(mobile, goodsRandomName);
        }

        return false;
    }

    /**
     * 以KEY方式验证token是否失效
     *
     * @param key 键值
     */
    public void validateTokenByKey(String key) {
        Long tokenSavedTimeStamp = redisUtil.get(key, Long.class);

        // 判断token是否过了有效期
        if (tokenSavedTimeStamp != null
                && (System.currentTimeMillis() - tokenSavedTimeStamp > CommonConstant.TOKEN_EFECTIVE_MILLISECONDS)) {
            // 失效了的清楚掉
            redisUtil.delete(key);
            // 如果token存在，且是过期的，则回馈redis库存
            goodsRedisStoreCache.incrStore(key.substring(key.lastIndexOf(":"), key.lastIndexOf("_")));
        }
    }

    protected String getKey(String mobile, String goodsRandomName) {
        String key = MessageFormat.format(CommonConstant.RedisKey.SPIKE_SUCCESS_TOKEN, mobile, goodsRandomName);
        return key;
    }

    public Set<String> getAllToken() {
        return redisUtil.keys(CommonConstant.RedisKey.SPIKE_SUCCESS_TOKEN_PREFIX + "*");
    }
}
