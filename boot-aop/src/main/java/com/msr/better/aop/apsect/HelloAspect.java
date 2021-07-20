package com.msr.better.aop.apsect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-30 00:00
 **/
@Aspect
@Component
public class HelloAspect {

    @Before("execution(* com.msr.better.aop.controller.HelloController.test1(..))")
    public void before(ProceedingJoinPoint joinPoint) {
        System.out.println(joinPoint);
        System.out.println("======================= Before ======================");
    }


}
