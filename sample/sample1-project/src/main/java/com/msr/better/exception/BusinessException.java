package com.msr.better.exception;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 21:57:07
 */
public class BusinessException extends RuntimeException{

    public BusinessException(String message) {
        super(message);
    }
}
