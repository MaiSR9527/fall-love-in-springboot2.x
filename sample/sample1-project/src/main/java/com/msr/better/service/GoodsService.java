package com.msr.better.service;

import com.msr.better.cache.*;
import com.msr.better.constant.MessageType;
import com.msr.better.dao.GoodsMapper;
import com.msr.better.domain.Goods;
import com.msr.better.exception.BusinessException;
import com.msr.better.mq.Message;
import com.msr.better.mq.MessageTrunk;
import com.msr.better.mq.MiaoshaRequestMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 22:00:22
 */
@Service
public class GoodsService {
    @Resource
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsBuyCurrentLimiter goodsBuyCurrentLimiter;

    @Autowired
    private MiaoshaSuccessTokenCache miaoshaSuccessTokenCache;

    @Autowired
    private MessageTrunk messageTrunk;

    @Autowired
    private MiaoshaFinishCache miaoshaFinishCache;

    @Autowired
    private MiaoshaHandlingListCache miaoshaHandlingListCache;

    @Autowired
    private GoodsInfoCacheWorker goodsInfoCacheWorker;

    /**
     * 根据goodsId获取随机名称(这样确保前端用户不能提前知道秒杀链接，降低被刷风险)
     *
     * @param goodsId 商品id
     */
    public String getGoodsRandomName(Integer goodsId) {
        Goods goods = goodsInfoCacheWorker.get(goodsId, Goods.class);
        long now = System.currentTimeMillis();

        // 已经开始了活动，则输出抢购链接
        if (goods.getStartTime().getTime() < now && now < goods.getEndTime().getTime()) {
            return goods.getRandomName();
        }

        return StringUtils.EMPTY;
    }

    /**
     * 获取抢购商品列表
     *
     * @return 结果
     */
    public List<Goods> getGoodsList() {
        return goodsMapper.selectAll();
    }

    /**
     * 获取商品详情
     *
     * @param goodsId 商品id
     * @return 商品
     */
    public Goods getDetail(Integer goodsId) {
        return goodsMapper.selectByPrimaryKey(goodsId);
    }

    /**
     * 做秒杀操作
     *
     * @param mobile          手机号
     * @param goodsRandomName 商品名
     */
    public void miaosha(String mobile, String goodsRandomName) {
        // 先看抢购是否已经结束了
        if (miaoshaFinishCache.isFinish(goodsRandomName)) {
            throw new BusinessException("您已经提交抢购，正在处理中");
        }

        // 先限流
        goodsBuyCurrentLimiter.doLimit(goodsRandomName, "啊呀，没挤进去");

        // 判断是否处理中(是否在处理列表中)
        if (miaoshaHandlingListCache.isInHandleList(mobile, goodsRandomName)) {
            throw new BusinessException("您已经提交过抢购，如果抢购成功请下单，否则耐心等待哦...");
        }

        // 请求消息推入处理队列，结束
        Message message = new Message(MessageType.SPIKE_MESSAGE, new MiaoshaRequestMessage(mobile, goodsRandomName));
        messageTrunk.put(message);

        // 加入正在处理列表
        miaoshaHandlingListCache.add2HanleList(mobile, goodsRandomName);

    }

    private Goods checkStore(String goodsRandomName) {
        Goods goods = goodsMapper.selectByRandomName(goodsRandomName);
        if (goods == null || goods.getStore() <= 0) {
            miaoshaFinishCache.setFinish(goodsRandomName);
            throw new RuntimeException("很遗憾，抢购已经结束了哟"); // 库存不足，抢购失败
        }
        return goods;
    }

    /**
     * 真正做减库存操作(这里没有采用存储过程的原因：这里并没有高并发，高并发已经在获取token时分流，所以此处没必要用存储过程)
     *
     * @param goodsId
     * @since 2017年3月20日 下午2:17:42
     */
    public Integer reduceStoreAndCreateOrder(String mobile, Integer goodsId) {
        Date orderTime = new Date();
        Map<String, Object> map = new HashMap<>();
        map.put("goodsId", goodsId);
        map.put("mobile", mobile);
        map.put("orderTime", orderTime);
        map.put("o_result", -2);
        map.put("o_order_id", -1);
        goodsMapper.doOrder(map);

        Integer result = (Integer) map.get("o_result");

        if (result != null && result == 1) {
            return (Integer) map.get("o_order_id");

        }
        throw new BusinessException("下单失败，来晚了");
    }

    /**
     * 真正的减库存、下单操作
     *
     * @param goodsId
     * @return 订单号
     */
    public Integer order(String mobile, Integer goodsId, String token) {
        // 先检查token有效性
        Goods goodsInfo = goodsInfoCacheWorker.get(goodsId, Goods.class);
        if (!miaoshaSuccessTokenCache.validateToken(mobile, goodsInfo.getRandomName(), token)) {
            throw new BusinessException("token不对，不能下单哦");
        }

        // 先检查库存，没有库存直接结束
        checkStore(goodsInfo.getRandomName());

        // 对于进来的客户做减库存+生成订单
        return reduceStoreAndCreateOrder(mobile, goodsId);
    }

}
