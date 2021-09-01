package com.msr.better.cache;

import com.msr.better.cache.base.CurrentLimiter;
import com.msr.better.constant.CommonConstant;
import com.msr.better.constant.MessageType;
import com.msr.better.dao.GoodsMapper;
import com.msr.better.mq.MessageMonitor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.MessageFormat;

/**
 * 商品购买限流器
 *
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 23:28:52
 */
@Component
public class GoodsBuyCurrentLimiter extends CurrentLimiter<String> {

    private final MessageMonitor messageMonitor;
    @Resource
    private GoodsMapper goodsMapper;

    public GoodsBuyCurrentLimiter(MessageMonitor messageMonitor) {
        this.messageMonitor = messageMonitor;
    }

    @Override
    protected String getLimiterName(String goodsRandomName) {
        return MessageFormat.format(CommonConstant.RedisKey.GOODS_STORE_BY_ID, goodsRandomName);
    }

    @Override
    protected int getLimit(String goodsRandomName) {
        return goodsMapper.selectByRandomName(goodsRandomName).getStore() * CommonConstant.CurrentLimitMultiple.GOODS_BUY;
    }

    @Override
    protected int getCurrentLimit() {
        return messageMonitor.getMessageLeft(MessageType.SPIKE_MESSAGE);
    }

}
