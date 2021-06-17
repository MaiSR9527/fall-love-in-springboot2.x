package com.msr.better;

import com.msr.better.bean.Article;
import com.msr.better.validate.ValidateGroup;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-17
 */
@RestController
public class TestController {

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

}
