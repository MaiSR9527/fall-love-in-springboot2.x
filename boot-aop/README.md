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

# 二、AOP开发入门

SpringBoot中通过注解的方式来声明切面，在开发上会简单很多。

添加依赖

```xml
	<parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.3.RELEASE</version>
    </parent>
	<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
    </dependencies>
```

## 2.1 编写一个Controller

HelloController

```java
@RestController
@RequestMapping("hello")
public class HelloController {

    @GetMapping("test1/{id}")
    public Object test1(@PathVariable("id")Integer id) {
        System.out.println(id);
        return "success";
    }
}
```

## 2.2 开发切面

```java
@Aspect
@Component
public class HelloAspect {

    /**
     * 定义一个切入点
     */
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
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            System.out.println("参数：" + args[i]);
        }
        // 被拦截的方法所在的类
        System.out.println(joinPoint.getTarget().getClass().getName());
        Object proceed = joinPoint.proceed();
        System.out.println("返回结果：" + proceed);
        return proceed;
    }
}
```

## 2.3 启动类

```java
@SpringBootApplication
public class AopApplication {

    public static void main(String[] args) {
        SpringApplication.run(AopApplication.class, args);
    }
}
```

## 2.4 测试

通过IDEA带的HTTP Client插件，然后查看程序的输出

```
GET http://localhost:8088/hello/test1/123
```

输出结果：

```
参数：123
com.msr.better.aop.controller.HelloController
======================= Before ======================
controller:123
======================= afterReturn ======================
======================= After ======================
返回结果：success
```

在没有发生异常的时候，切面是以这样的顺序执行的

> @Around中执行joinPoint.proceed() 前面的代码被执行
>
> @Before 前置通知被执行
>
> Object proceed = joinPoint.proceed();    放行
>
> 执行连接点方法 ，例如上面例子中的com.msr.better.aop.controller.HelloController.test1方法
>
> @AfterReturn  返回通知被执行
>
> @After 后置通知被执行
>
> @Around中执行joinPoint.proceed() 后面的代码被执行
>
> 
> 代码中 return proceed 把结果返回

注意，ProceedingJoinPoint joinPoint 参数只能在环绕通知中引用，在其他通知当作参数引用时会爆一下的错误。可见Spring中切面的放行是在环绕通知中做的。

```
ProceedingJoinPoint is only supported for around advice
```

ProceedingJoinPoint该类是一个接口，在环绕通知中使用的实现类是MethodInvocationProceedingJoinPoint，其中比较常用的一些方法：

* getTarget()   获取被代理的对象，即连接点所在的类的实例
* getSignature() 封装了签名信息的对象。通过Signature对象可以进一步拿到一些信息：
  * 可以拿到当前连接点的方法名(getName方法)
  * 可以拿到连接点所在类的全类名(getDeclaringTypeName方法)
  * 连接点的详细信息(toLongString方法)。例如上面的例子调用的话结果是：`public java.lang.Object com.msr.better.aop.controller.HelloController.test1(java.lang.Integer)`
  * 与toLongString方法相反的toShortString方法则会得到：`HelloController.test1(..)`
* getArgs()：获取连接点的所有参数
* getThis()：也是得到被代理的对象，getTarget方法就是进一步调用getThis方法得到代理对象的

# 三、多切面执行顺序

如果定义了多个切面，而且Spring是支持这些切面都拦截了同样的连接点。因此我们有必要知道这些切面的运行顺序。

## 3.1 定义多个切面

切面1

```java
@Aspect
public class MyAspect1 {
    @Pointcut("execution(public * com.msr.better.aop.controller.HelloController.test2(..))")
    public void pointCut1() {

    }
    @Before("pointCut1()")
    public void b1() {
        System.out.println("MyAspect1 before");
    }
}
```

切面2

```java
@Aspect
public class MyAspect2 {
    @Pointcut("execution(public * com.msr.better.aop.controller.HelloController.test2(..))")
    public void pointCut2() {

    }
    @Before("pointCut2()")
    public void b2() {
        System.out.println("MyAspect2 before");
    }
}
```

切面3

```java
@Aspect
public class MyAspect3 {

    @Pointcut("execution(public * com.msr.better.aop.controller.HelloController.test2(..))")
    public void pointCut3() {

    }

    @Before("pointCut3()")
    public void b3() {
        System.out.println("MyAspect3 before");
    }
}
```

在启动类里面注入

```java
@Bean
public MyAspect1 myAspect1() {
    return new MyAspect1();
}

@Bean
public MyAspect2 myAspect2() {
    return new MyAspect2();
}

@Bean
public MyAspect3 myAspect3() {
    return new MyAspect3();
}
```

test2方法

```java
@GetMapping("test2")
public Object test2() {
    return "success";
}
```

测试，利用IDEA自带的功能

> GET http://localhost:8088/hello/test2
>
> 输出：
>
> MyAspect1 before
> MyAspect2 before
> MyAspect3 before

如果改变这三个Bean的注入顺序，列如

```java
@Bean
public MyAspect3 myAspect3() {
    return new MyAspect3();
}
@Bean
public MyAspect1 myAspect1() {
    return new MyAspect1();
}

@Bean
public MyAspect2 myAspect2() {
    return new MyAspect2();
}
```

再次测试，结果顺序也会变。

> MyAspect3 before
> MyAspect1 before
> MyAspect2 before

很明显这来控制切面的执行顺序很不可控，一般我们在切面的的类型上添加注解@Order，例如：

> @Aspect
> @Order(1)
> public class MyAspect1
>
> @Aspect
> @Order(2)
> public class MyAspect2
>
> @Aspect
> @Order(3)
> public class MyAspect3 

或者切面实现Orderd接口，并且实现getOrder()方法，例如

```java
@Aspect
public class MyAspect3 implements Orderd{
    @Override
    public int getOrder(){
        // 指定顺序
        return 3;
    }
}
```

# 四、总结

关于SpringBoot中使用AOP就介绍到这里，或许跟使用纯Spring Framework编写相比，可能也就大同小异吧。使用AOP增强业务方法在现实开发中也是很常见的，例如：通过自定义注解+AOP+多数据，实现数据源的动态切换跟读写分离。