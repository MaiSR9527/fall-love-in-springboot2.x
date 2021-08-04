---
layout: springboot
title: SpringBoot中单元测试、应用部署和监控
date: 2021-05-12 9:10:12
categories: java
tags: SpringBoot
---

# 

本文中用到的依赖：

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
  	<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-hateoas</artifactId>
</dependency>
```

# 一、Spring Boot单元测试

在我们开发Web应用时，经常会直接去观察结果进行测试。虽然也是一种方式，但是并不严谨。作为开发者编写测试代码来测试自己所写的业务逻辑是，以提高代码的质量、降低错误方法的概率以及进行性能测试等。经常作为开发这写的最多就是单元测试。引入`spring-boot-starter-test`SpringBoot的测试依赖。该依赖会引入JUnit的测试包，也是我们用的做多的单元测试包。而Spring Boot在此基础上做了很多增强，支持很多方面的测试，例如JPA，MongoDB，Spring MVC（REST）和Redis等。

接下来主要是测试业务逻辑层的代码，REST和Mock测试。

## 1.1 JUnit简介

JUnit是一个Java语言的单元测试框架。它由Kent Beck和Erich Gamma建立，逐渐成为源于Kent Beck的sUnit的xUnit家族中最为成功的一个。 JUnit有它自己的JUnit扩展生态圈。多数Java的开发环境都已经集成了JUnit作为单元测试的工具。 

| JUnit相关概念 | 含义                                                         |
| ------------- | ------------------------------------------------------------ |
| 测试          | 一个以@Test注释的方法定义一个测试，运行这个方法，JUnit会创建一个包含类的实例，然后再调用这个被注释的方法。 |
| 测试类        | 包含多个@Test方法的一个类                                    |
| Assert        | 定义想测试的条件，当条件成立时，assert 方法保持沉默，条件不成立时，则抛出异常 |
| Suite         | Suite允许将测试类归类成一组                                  |
| Runner        | Runner类用于运行测试，JUnit4是向后兼容的，可以运行JUnit3的测试实例 |

这里使用的是JUnit4.x版本，JUnit中有两个重要的类Assume+Assert，以及重要的注解：BeforeClass、AfterClass、After、Before、Test和Ignore。BeforeClass和AfterClass在每个类的开始和结束的时候运行，需要static修饰方法。而Before和After则是在每个测试方法的开始和结束的时候运行。

# 二、Spring Boot应用部署

TODO

# 三、监控Spring Boot应用

TODO
