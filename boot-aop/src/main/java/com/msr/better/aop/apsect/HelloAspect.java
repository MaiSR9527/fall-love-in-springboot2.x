package com.msr.better.aop.apsect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-30 00:00
 **/
@Aspect
@Component
public class HelloAspect {

    @Pointcut("execution(* com.msr.better.aop.controller.HelloController.test1(..))")
    public void pointCut(){

    }

    /**
     * 前置通知
     *
     * @param joinPoint
     */
    @Before("pointCut()")
    public void before() {
        System.out.println("======================= Before ======================");
    }

    /**
     * 后置通知
     */
    @After("pointCut()")
    public void after() {
        System.out.println("======================= After ======================");
    }

    /**
     * 返回通知
     */
    @AfterReturning("pointCut()")
    public void afterReturn() {
        System.out.println("======================= afterReturn ======================");
    }

    /**
     * 异常通知
     */
    @AfterThrowing("pointCut()")
    public void afterThrow() {
        System.out.println("======================= afterThrow ======================");
    }

    /**
     * 环绕通知
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取拦截的方法的参数
        System.out.println(joinPoint.getThis().toString());
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            System.out.println("参数：" + args[i]);
        }
        // 被拦截的方法所在的类
        System.out.println(joinPoint.getTarget().getClass().getName());
        System.out.println(joinPoint.getSignature().getName());
        System.out.println(joinPoint.getSignature().getDeclaringTypeName());
        System.out.println(joinPoint.getSignature().toLongString());
        System.out.println(joinPoint.getSignature().toShortString());
        Object proceed = joinPoint.proceed();
        System.out.println("返回结果：" + proceed);
        return proceed;
    }


}
