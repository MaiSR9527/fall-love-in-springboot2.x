package top.maishuren.impl;

import org.apache.dubbo.config.annotation.DubboService;
import top.maishuren.api.DubboServiceB;

/**
 * @author maisrcn@qq.com
 * @since 2024-01-30
 */
@DubboService
public class DubboServiceBImpl implements DubboServiceB {
    @Override
    public String helloB() {
        return "hello this is service b";
    }
}
