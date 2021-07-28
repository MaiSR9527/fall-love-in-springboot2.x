package com.msr.better.aop.apsect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-30 00:00
 **/
@Aspect
@Order(3)
public class MyAspect3 {

    @Pointcut("execution(public * com.msr.better.aop.controller.HelloController.test2(..))")
    public void pointCut3() {

    }

    @Before("pointCut3()")
    public void b3() {
        System.out.println("MyAspect3 before");
    }
}
