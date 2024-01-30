package top.maishuren.impl;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import top.maishuren.api.DubboServiceA;
import top.maishuren.api.DubboServiceB;

/**
 * @author maisrcn@qq.com
 * @since 2024-01-30
 */
@DubboService
public class DubboServiceAImpl implements DubboServiceA {
    @DubboReference(check = false)
    private DubboServiceB dubboServiceB;

    @Override
    public String helloA() {
        return "this is service a and say to service b, reply from service b:" + dubboServiceB.helloB();
    }
}
