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

# 三、切入点表达式

切入点表达式通常都会是从宏观上定位一组方法，和具体某个通知的注解结合起来就能够确定对应的连接点。AOP(面向切面编程)可以说是一种编程思想，AspectJ是其中的一种具体实现，Spring AOP使用的就是AspectJ。

AspectJ中支持的切入点表达式

| 项目类型      | 描述                                      |
| ------------- | ----------------------------------------- |
| arg()         | 限定连接点方法参数                        |
| @args()       | 通过链接上方法参数上的注解进行限定        |
| execution()   | 听雨匹配链接点的执行方法                  |
| this()        | 限定连接点匹配AOP代理Bean引用为指定的类型 |
| target        | 目标对象（即代理对象）                    |
| @target()     | 限定目标对象的配置了指定的注解            |
| within        | 限制连接点匹配指定的类型                  |
| @within()     | 限定连接点带有匹配注解类型                |
| @annotation() | 限定带有指定注解的连接点                  |

切入点表达式的格式：

> execution("权限修饰符 返回值类型 简单类名/全类名.方法名(参数列表)")

TODO

# 四、AOP引入增强

TODO

# 五、多切面执行顺序

TODO

# 六、总结

TODO