package com.msr.better.mq;


import java.io.Serializable;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 23:33:30
 */
public class MiaoshaRequestMessage implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 5810025604361901986L;

    /**
     * 手机号，标识用户唯一身份
     */
    private String mobile;

    /**
     * 秒杀商品编号
     */
    private String goodsRandomName;

    public MiaoshaRequestMessage() {
        super();
    }

    public MiaoshaRequestMessage(String mobile, String goodsRandomName) {
        super();
        this.mobile = mobile;
        this.goodsRandomName = goodsRandomName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGoodsRandomName() {
        return goodsRandomName;
    }

    public void setGoodsRandomName(String goodsRandomName) {
        this.goodsRandomName = goodsRandomName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MiaoshaRequestMessage [mobile=");
        builder.append(mobile);
        builder.append(", goodsRandomName=");
        builder.append(goodsRandomName);
        builder.append("]");
        return builder.toString();
    }

}