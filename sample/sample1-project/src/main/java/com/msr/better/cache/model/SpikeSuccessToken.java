package com.msr.better.cache.model;

/**
 * 秒杀成功token（用来下单做验证）
 *
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 22:11:08
 */
public class SpikeSuccessToken {
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 商品ID
     */
    private Integer goodsId;

    /**
     * 成功占redis库存时间
     */
    private long time;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MiaoshaSuccessToken [mobile=");
        builder.append(mobile);
        builder.append(", goodsId=");
        builder.append(goodsId);
        builder.append(", time=");
        builder.append(time);
        builder.append("]");
        return builder.toString();
    }
}
