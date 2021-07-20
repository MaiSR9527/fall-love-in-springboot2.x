---
layout: springboot
title: SpringBoot中使用约定编程AOP
date: 2021-06-18 9:10:12
categories: 
  - java
tags: SpringBoot
---



# 一、AOP的术语和流程

# 1.1 术语

* 连接点(join point)：具体被拦截的对象。在spring中支持的是方法，拦截的对象是方法。譬如在做web开发的时候，我们相对每个controller的方法都进行拦截，打印一下日志。这时候就可以用到AOP进行拦截，这些controller里面的方法就是连接点。
* 切点(point cut)：在上面说到，有时候我们要拦截的不仅是单个方法，可能是多个类中的不同方法，这时候我们就需要通过像正则边大师和指示器的规则去定义，去适配连接点。这就是切点。
* 通知(advice)：就是安装约定的流程方法，分为前置通知(before advice)、后置通知(after advice)、环绕通知(around advicd)、事后返回通知(afterReturning advice)和异常通知(afterThrowing advice)，根据约定织入到流程中。
* 目标对象(target)：被代理的对象，例如有一个HelloController类，通过AOP拦截了这个类里面的一个或多个方法。那么这个HelloController类就是目标对象，它被代理了。
* 引入(introduction)：是指引入新的类和其方法，增强现有的Bean的功能。
* 织入(weaving)：通过动态代理，为目标对象生成一个代理对象，然后把切入点定义匹配到的连接点进行拦截，并把各种通知织入到这些连接点。
* 切面(asopect)：是一个可以定义切点、通知和引入的一个类。Spring AOP将通过该类的信息来增强Bean的功能。

Spring AOP流程：

![](https://cdn.jsdelivr.net/gh/MaiSR9527/blog-pic/spring/spring-aop.jpg)

# 二、AOP详细开发

SpringBoot中通过注解的方式来配置切面，在开发上会简单很多。

返回类型 包名.类名.方法名(参数)