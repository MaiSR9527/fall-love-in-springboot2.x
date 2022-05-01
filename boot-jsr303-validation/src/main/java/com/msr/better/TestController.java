package com.msr.better;

import com.msr.better.bean.Article;
import com.msr.better.validate.ValidateGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-17
 */
@RestController
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

//    @PostMapping("test")
//    public Object test(@Valid @RequestBody Article article, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            // 这里用一个Map来模拟开发中的一个返回对象
//            HashMap<String, Object> map = new HashMap<>(3);
//
//            HashMap<String, String> data = new HashMap<>(16);
//            bindingResult.getFieldErrors().stream().forEach(item -> {
//                String message = item.getDefaultMessage();
//                String field = item.getField();
//                data.put(field, message);
//            });
//
//            map.put("code", 400);
//            map.put("message", "参数不合法");
//            map.put("data", data);
//
//            return map;
//        }else {
//            // 校验成功，继续业务逻辑
//            // ....
//            return article;
//        }
//    }

    @PostMapping("add")
    public Object add(@Validated(value = {ValidateGroup.AddValidate.class}) @RequestBody Article article) {
        return article;
    }

    @PostMapping("update")
    public Object update(@Validated(value = {ValidateGroup.UpdateValidate.class}) @RequestBody Article article) {
        return article;
    }

    @PutMapping("update/status")
    public Object updateStatus(@Validated(value = {ValidateGroup.ArticleStatusValidate.class}) @RequestBody Article article) {
        return article;
    }

    @GetMapping("test/client")
    public Object testClient(HttpServletRequest request, HttpServletResponse response) {
        logger.info("coming in {}", request.getRemoteAddr());
        HashMap<Object, Object> map = new HashMap<>();
        map.put("error_code", 500);
        map.put("msg", "inner server error");
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        if (request.getRemoteAddr().equals("127.0.0.1")) {
//            throw new RuntimeException("ni hao");
//        }
        response.setStatus(503);
        return map;
    }

}
