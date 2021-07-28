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
@Order(2)
public class MyAspect2 {

    @Pointcut("execution(public * com.msr.better.aop.controller.HelloController.test2(..))")
    public void pointCut2() {

    }

    @Before("pointCut2()")
    public void b2() {
        System.out.println("MyAspect2 before");
    }

}
