package com.msr.better.mq;

import com.msr.better.cache.GoodsRedisStoreCache;
import com.msr.better.cache.MiaoshaFinishCache;
import com.msr.better.cache.MiaoshaSuccessTokenCache;
import com.msr.better.cache.UserBlackListCache;
import com.msr.better.constant.MessageType;
import com.msr.better.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 23:33:11
 */
public class MiaoshaRequestHandler extends AbstractMessageHandler<MiaoshaRequestMessage> {
    private static final Logger logger = LoggerFactory.getLogger(MiaoshaRequestHandler.class);

    @Autowired
    private GoodsRedisStoreCache goodsRedisStoreCache;

    @Autowired
    private MiaoshaSuccessTokenCache miaoshaSuccessTokenCache;

    @Autowired
    private UserBlackListCache userBlackListCache;

    @Autowired
    private MiaoshaFinishCache miaoshaFinishCache;

    public MiaoshaRequestHandler() {
        // 说明该handler监控的消息类型；失败重试次数设定为MAX_VALUE
        super(MessageType.SPIKE_MESSAGE, MiaoshaRequestMessage.class, Integer.MAX_VALUE);
    }

    /**
     * 监听到消息后处理方法
     */
    public void handle(MiaoshaRequestMessage message) {
        long startTimeN = System.currentTimeMillis();
        long startTime1 = System.currentTimeMillis();
        // 查看请求用户是否在黑名单中
        if (userBlackListCache.isIn(message.getMobile())) {
            logger.error(message.getMobile() + "检测为黑名单用户，拒绝抢购");
            return;
        }
        // logger.error("1耗时：" + (System.currentTimeMillis() - startTime));
        long startTime2 = System.currentTimeMillis();
        // 先看抢购是否已经结束了
        if (miaoshaFinishCache.isFinish(message.getGoodsRandomName())) {
            logger.error("抱歉，您来晚了，抢购已经结束了");
            return;
        }
        // logger.error("2耗时：" + (System.currentTimeMillis() - startTime));
        long startTime3 = System.currentTimeMillis();
        // 先减redis库存
        if (!goodsRedisStoreCache.decrStore(message.getGoodsRandomName())) {
            // 减库存失败
            throw new BusinessException("占redis名额失败，等待重试");
        }
        // logger.error("3耗时：" + (System.currentTimeMillis() - startTime));
        long startTime4 = System.currentTimeMillis();
        // 减库存成功：生成下单token，并存入redis供前端获取
        String token = miaoshaSuccessTokenCache.genToken(message.getMobile(), message.getGoodsRandomName());
        // logger.error("4耗时：" + (System.currentTimeMillis() - startTime));
        long startTime5 = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("step1:").append(startTime2 - startTime1).append("step2:").append(startTime3 - startTime2)
                .append("step3:").append(startTime4 - startTime3).append("step4:").append(startTime5 - startTime4)
                .append("all:").append(startTime5 - startTime1).append(",token:").append(token);
        logger.error(sb.toString());

    }

    public void handleFailed(MiaoshaRequestMessage obj) {
        StringBuilder sb = new StringBuilder();
        sb.append("msg:[").append(obj).append("], 超过失败次数，停止重试。");
        logger.warn(sb.toString());

    }

}

