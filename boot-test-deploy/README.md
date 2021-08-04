---
layout: springboot
title: SpringBoot中单元测试、应用部署和监控
date: 2021-05-12 9:10:12
categories: java
tags: SpringBoot
---

本文中用到的依赖：

```xml
<dependency>
  	<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-hateoas</artifactId>
</dependency>
```

# 一、关于JUnit的一些东西

在我们开发Web应用时，经常会直接去观察结果进行测试。虽然也是一种方式，但是并不严谨。作为开发者编写测试代码来测试自己所写的业务逻辑是，以提高代码的质量、降低错误方法的概率以及进行性能测试等。经常作为开发这写的最多就是单元测试。引入`spring-boot-starter-test`SpringBoot的测试依赖。该依赖会引入JUnit的测试包，也是我们用的做多的单元测试包。而Spring Boot在此基础上做了很多增强，支持很多方面的测试，例如JPA，MongoDB，Spring MVC（REST）和Redis等。

接下来主要是测试业务逻辑层的代码，REST和Mock测试。

## 1.1 JUnit介绍

JUnit是一个Java语言的单元测试框架。它由Kent Beck和Erich Gamma建立，逐渐成为源于Kent Beck的sUnit的xUnit家族中最为成功的一个。 JUnit有它自己的JUnit扩展生态圈。多数Java的开发环境都已经集成了JUnit作为单元测试的工具。 

| JUnit相关概念 | 含义                                                         |
| ------------- | ------------------------------------------------------------ |
| 测试          | 一个以@Test注释的方法定义一个测试，运行这个方法，JUnit会创建一个包含类的实例，然后再调用这个被注释的方法。 |
| 测试类        | 包含多个@Test方法的一个类                                    |
| Assert        | 定义想测试的条件，当条件成立时，assert 方法保持沉默，条件不成立时，则抛出异常 |
| Suite         | Suite允许将测试类归类成一组                                  |
| Runner        | Runner类用于运行测试，JUnit4是向后兼容的，可以运行JUnit3的测试实例 |

这里使用的是JUnit4.x版本，JUnit中有两个重要的类Assume+Assert，以及重要的注解：BeforeClass、AfterClass、After、Before、Test和Ignore。BeforeClass和AfterClass在每个类的开始和结束的时候运行，需要static修饰方法。而Before和After则是在每个测试方法的开始和结束的时候运行。

代码片段：TestDeployApplication.class是自己编写的Spring Boot启动类。

```java
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestDeployApplication.class})
public class UnitTest1 {

    @BeforeClass
    public static void beforeClass() {
        System.out.println("=================BeforeClass================");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("=================AfterClass================");
    }

    @Before
    public void beforeTest() {
        System.out.println("before test");
    }

    @After
    public void afterTest() {
        System.out.println("after test");
    }

    @Test
    public void test1() {
        System.out.println("test1");
    }

    @Test
    public void test2() {
        System.out.println("test2");
    }
}

```

## 1.2 JUnit的Assert类

Assert类中常用的方法：

* assertEquals("提示信息",A,B)：当判断A是否等于B，不等于就抛出错误。比较对象是调用的是equals()方法。
* assertSame("提示信息",A,B)：判断对象是否相同。
* assertTrue("提示信息",A)：判断条件A是否为真。
* assertFalse("提示信息",A)：判断条件是否为假。
* assertNotNull("提示信息",A)：判断对象是否不为空。
* assertNull("提示信息",A)：判断对象是否不为空。
* assertArrayEqual("提示信息",A,B)：判断数组A和数组B是否相等。

## 1.3 JUnit的Suite

JUnit的Suite设计就是一次性运行一个或多个测试用例，Suite可以看作是一个容器，用来把测试类归类在一起，并把他们作为一个集合来运行，运行器启动Suite。

```java
@RunWith(Suite.class)
@SuiteClasses({UnitTest1.class,UnitTest2.class})
public class MainTest{
    
}
```

# 二、Spring Boot单元测试

添加需要的依赖

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

## 2.1 Spring Boot测试依赖提供的测试范围

引入了`spring-boot-starter-test`继承了很多的测试库：

* JUnit，标准的单元测试Java程序。
* Spring Test和Spring Boot Test，对Spring Boot应用的单元测试。
* Mockito，Java Mock测试框架，用于模拟任何Spring管理的Bean。例如在单元测试中，模拟一个第三方系统接口返回的数据，而不用真正地去请求第三方接口。
* AssertJ，一个assertion库，同时提供了更加多的期望值与测试返回值的比较方式。
*  Hamcrest，库的匹配对象。
* JSONassert，对JSON对象或者JSON字符串断言的库。
* JSONPath，提供向XPath那样的符号来获取JSON字段。

## 2.2 Spring Boot单元测试的脚手架

在使用spring.io创建的Spring Boot工程中，就默认常见了一个单元测试的类。

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class UnitTest1 {
	@Test
    public void contextLoads(){
        
    }
}
```

`@RunWith`是JUnit中的注解，用来通知JUnit单元测试框架不要使用内置的方式进行单元测试，向上面的写法，就是指定使用`SpringRunner`类来提供单元测试。

`@SpringBootTest`注解则是用于Spring Boot应用的测试，默认会分局报名逐级往上查找Spring Boot主程序，也就是`@SpringBootApplocation`注解，并在单元测试启动的时候启动该类来创建Spring上下文。所以我们在对Spring Boot应用进行单元测试的时候，在日志输出都可以看到Spring Boot应用的启动日志。

## 2.3 对Service层代码测试

```java
```





## 2.4 MVC的测试



## 2.5 Mock测试



# 四、面向数据库的单元测试



# 三、Spring Boot应用部署

TODO

# 三、监控Spring Boot应用

TODO
