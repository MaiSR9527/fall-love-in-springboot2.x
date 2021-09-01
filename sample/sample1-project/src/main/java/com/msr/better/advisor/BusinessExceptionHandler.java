package com.msr.better.advisor;

import com.msr.better.dto.Result;
import com.msr.better.exception.BusinessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 21:55:09
 */
@ControllerAdvice
public class BusinessExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result handlerBusinessException(BusinessException e) {

        return null;
    }
}
