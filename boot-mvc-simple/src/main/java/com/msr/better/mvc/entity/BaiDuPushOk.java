package com.msr.better.mvc.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022-04-27 21:50
 **/
@Data
public class BaiDuPushOk implements Serializable {

    private static final long serialVersionUID = 6261079339612259761L;
    // 成功推送的数目
    private Integer success;
    // 今天剩余可推送的数目
    private Integer remain;
    // 由于不是本站url而未处理的url列表
    private List<String> noSameSite;
    // 不合法的url列表
    private List<String> notValid;
    // 推送的网站，即域名
    private String webSite;
}
