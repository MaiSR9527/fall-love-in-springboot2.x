package com.msr.better.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 秒杀接口
 *
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 21:02:51
 */
@RestController
@RequestMapping(value = "/spike/")
public class SpikeController {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private MiaoshaSuccessTokenCache miaoshaSuccessTokenCache;

    /**
     * 秒杀接口
     *
     * @param mobile
     * @param goodsRandomName
     * @return
     */
    @Intercept(value = {UserInterceptor.class})
    // @Intercept(value = { ExecuteTimeInterceptor.class })
    @RequestMapping(value = "miaosha")
    public String miaosha(String mobile, String goodsRandomName) {
        Assert.notNull(goodsRandomName, "商品名不能为空");
        Assert.notNull(mobile, "手机号不能为空");

        goodsService.miaosha(mobile, goodsRandomName);

        // 为什么要返回mobile，为了方便jmeter测试
        return mobile;
    }

    /**
     * 获取秒杀商品的链接
     *
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "{goodsId}/getMiaoshaGoodsLink")
    public String getMiaoshaGoodsLink(Integer goodsId) {
        return goodsService.getGoodsRandomName(goodsId);
    }

    /**
     * 查询是否秒杀成功
     *
     * @param mobile
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "miaoshaResult")
    public String isMiaoshaSuccess(String mobile, String goodsRandomName) {
        // 直接取缓存查询是否有成功的记录生成
        return miaoshaSuccessTokenCache.queryToken(mobile, goodsRandomName);
    }

    /**
     * 下单
     *
     * @param mobile
     * @param goodsId
     * @param token
     */
    @RequestMapping(value = "order")
    public Integer order(String mobile, Integer goodsId, String token) {
        return goodsService.order(mobile, goodsId, token);
    }

    /**
     * 查询系统时间
     *
     * @return
     * @category 查询系统时间
     */
    @RequestMapping(value = "time/now")
    @ResponseBody
    public Long time() {
        return System.currentTimeMillis();
    }
}
