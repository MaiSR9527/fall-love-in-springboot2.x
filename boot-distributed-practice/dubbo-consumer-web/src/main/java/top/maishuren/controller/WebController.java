package top.maishuren.controller;

import com.msr.better.common.util.TraceIdUtil;
import com.yomahub.tlog.context.TLogContext;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.maishuren.api.DubboServiceA;

/**
 * @author maisrcn@qq.com
 * @since 2024-01-30
 */
@RestController
@RequestMapping("web")
public class WebController {

    @DubboReference(check = false)
    private DubboServiceA dubboServiceA;

    @GetMapping("trace")
    public String trace() {
        TLogContext.putTraceId(TraceIdUtil.uuid_timestamp());
        return dubboServiceA.helloA();
    }
}
