package com.msr.better.mvc.controller.push;

import com.msr.better.mvc.entity.BaiDuPushOk;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022-04-27 22:03
 **/
@Slf4j
@RestController
@RequestMapping("push")
public class PushController {
    @Autowired
    private BaiDuPush<BaiDuPushOk> baiduPush;

    @GetMapping("baidu")
    public Object getA() {
        BaiDuPushOk baiDuPushOk = null;
        try {
            baiDuPushOk = baiduPush.pushToBaidu(BaiDuPushOk.class);
            log.info("push success:{}", baiDuPushOk);
        } catch (Exception e) {
            log.error(e.getClass().getName());
            e.printStackTrace();
        }
        return baiDuPushOk;
    }
}
