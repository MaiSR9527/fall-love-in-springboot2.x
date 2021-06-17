package com.msr.better.advice;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-17
 */
@RestControllerAdvice
public class CustomExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidateException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        // 这里用一个Map来模拟开发中的一个返回对象
        HashMap<String, Object> map = new HashMap<>(3);

        HashMap<String, String> data = new HashMap<>(16);
        bindingResult.getFieldErrors().stream().forEach(item -> {
            String message = item.getDefaultMessage();
            String field = item.getField();
            data.put(field, message);
        });

        map.put("code", 400);
        map.put("message", "参数不合法");
        map.put("data", data);

        return map;
    }
}
