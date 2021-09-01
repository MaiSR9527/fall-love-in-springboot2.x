package com.msr.better.task;

import com.msr.better.cache.GoodsRedisStoreCache;
import com.msr.better.dao.GoodsMapper;
import com.msr.better.domain.Goods;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品redis库存初始化任务
 *
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 21:30:44
 */
@Component
public class GoodsRedisStoreInitTask {
    private final GoodsRedisStoreCache goodsRedisStore;
    @Resource
    private GoodsMapper goodsMapper;

    public GoodsRedisStoreInitTask(GoodsRedisStoreCache goodsRedisStore) {
        this.goodsRedisStore = goodsRedisStore;
    }

    /**
     * 每隔1分钟触发一次
     */
    @Scheduled(cron = "0 0/1 * * * ? ")
    public void doInit() {
        List<Goods> goods = goodsMapper.selectAll();
        for (Goods item : goods) {
            goodsRedisStore.doInit(item);

        }
    }

}
