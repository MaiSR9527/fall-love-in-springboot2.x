package com.msr.better.task;

import com.msr.better.cache.MiaoshaSuccessTokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 商品token过期清理任务，过期的token自动释放redis库存
 *
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 21:31:31
 */
@Component
public class GoodsTokenExpireClearTask {
    @Autowired
    private MiaoshaSuccessTokenCache miaoshaSuccessTokenCache;

    /**
     * 每隔1分钟触发一次
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void doClear() {
        Set<String> keys = miaoshaSuccessTokenCache.getAllToken();
        for (String key : keys) {
            //验证token是否过期，过期了自动释放redis库存
            miaoshaSuccessTokenCache.validateTokenByKey(key);
        }
    }


}
