package com.msr.better.controller;


import com.msr.better.cache.SpikeSuccessTokenCache;
import com.msr.better.service.GoodsService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping(value = "/goods")
public class SpikeController {
    private final GoodsService goodsService;

    private final SpikeSuccessTokenCache spikeSuccessTokenCache;

    public SpikeController(GoodsService goodsService, SpikeSuccessTokenCache spikeSuccessTokenCache) {
        this.goodsService = goodsService;
        this.spikeSuccessTokenCache = spikeSuccessTokenCache;
    }

    /**
     * 秒杀接口
     *
     * @param mobile          用户标识
     * @param goodsRandomName 商品标识
     * @return
     */
//    @Intercept(value = {UserInterceptor.class})
    // @Intercept(value = { ExecuteTimeInterceptor.class })
    @RequestMapping(value = "spike")
    public String spike(String mobile, String goodsRandomName) {
        Assert.notNull(goodsRandomName, "商品名不能为空");
        Assert.notNull(mobile, "手机号不能为空");

        goodsService.spike(mobile, goodsRandomName);

        // 为什么要返回mobile，为了方便jmeter测试
        return mobile;
    }

    /**
     * 获取秒杀商品的链接
     *
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "{goodsId}/getSpikeGoodsLink")
    public String getSpikeGoodsLink(@PathVariable Integer goodsId) {
        return goodsService.getGoodsRandomName(goodsId);
    }

    /**
     * 查询是否秒杀成功
     *
     * @param mobile
     * @param goodsId
     * @return
     */
    @RequestMapping(value = "spikeResult")
    public String isSpikeSuccess(String mobile, String goodsRandomName) {
        // 直接取缓存查询是否有成功的记录生成
        return spikeSuccessTokenCache.queryToken(mobile, goodsRandomName);
    }

    /**
     * 下单
     *
     * @param mobile  用户标识
     * @param goodsId 商品id
     * @param token   token
     */
    @RequestMapping(value = "order")
    public Integer order(String mobile, Integer goodsId, String token) {
        return goodsService.order(mobile, goodsId, token);
    }

    /**
     * 查询系统时间
     *
     * @return 系统时间
     */
    @RequestMapping(value = "time/now")
    @ResponseBody
    public Long time() {
        return System.currentTimeMillis();
    }
}
