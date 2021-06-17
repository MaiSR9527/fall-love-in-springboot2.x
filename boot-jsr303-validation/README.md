---
layout: springboot
title: SpringBoot中使用Bean Validation校验参数
date: 2021-05-18 9:10:12
categories: 
  - java
tags: SpringBoot
---

# 一、简述

JSR-303 是 JAVA EE 6 中的一项子规范，叫做 Bean Validation。Bean Validation 为 Java Bean 验证定义了相应的元数据模型和 API。用于对Java Bean 中的字段的值进行验证,使得基本的验证逻辑可以从业务代码中脱离出来。

常用的注解：

| Constraint                  | 详细信息                                                 | 作用类型                                                     |
| --------------------------- | -------------------------------------------------------- | ------------------------------------------------------------ |
| @Null                       | 被注释的元素必须为 null                                  | 引用类型                                                     |
| @NotNull                    | 被注释的元素必须不为 null                                | 引用类型                                                     |
| @AssertTrue                 | 被注释的元素必须为 true                                  | boolean                                                      |
| @AssertFalse                | 被注释的元素必须为 false                                 | boolean                                                      |
| @Min(value)                 | 被注释的元素必须是一个数字，其值必须大于等于指定的最小值 | byte、short、int、long及对应的包装类型以及BigDecimal、BigInteger |
| @Max(value)                 | 被注释的元素必须是一个数字，其值必须小于等于指定的最大值 | byte、short、int、long及对应的包装类型以及BigDecimal、BigInteger |
| @DecimalMin(value)          | 被注释的元素必须是一个数字，其值必须大于等于指定的最小值 | byte、short、int、long及对应的包装类型以及BigDecimal、BigInteger、String |
| @DecimalMax(value)          | 被注释的元素必须是一个数字，其值必须小于等于指定的最大值 | byte、short、int、long及对应的包装类型以及BigDecimal、BigInteger、String |
| @Size(max, min)             | 被注释的元素的大小必须在指定的范围内                     | String、Collection、Map和数组                                |
| @Digits (integer, fraction) | 被注释的元素必须是一个数字，其值必须在可接受的范围内     | byte、short、int、long及各自的包装类型以及BigDecimal、BigInteger、String |
| @Past                       | 被注释的元素必须是一个过去的日期                         | java.util.Date,java.util.Calendar                            |
| @Future                     | 被注释的元素必须是一个将来的日期                         | java.util.Date,java.util.Calendar                            |
| @Pattern(regex=)            | 被注释的元素必须符合指定的正则表达式                     | String                                                       |
| @Valid                      | 被注释的元素需要递归验证                                 | 引用对象                                                     |


还有hibernate Validator新增的注解

| Constraint                     | 详细信息                                        | 作用类型                                                     |
| ------------------------------ | ----------------------------------------------- | ------------------------------------------------------------ |
| @Email                         | 被注释的元素必须是电子邮箱地址                  | String                                                       |
| @Length(min=下限, max=上限)    | 被注释的字符串的大小必须在指定的范围内          | String                                                       |
| @NotEmpty                      | 被注释的元素的必须非空并且size大于0             | String、Collection、Map和数组                                |
| @NotBlank                      | 被注释的元素必须不为空且不能全部为’ '(空字符串) | String                                                       |
| @Range(min=最小值, max=最大值) | 被注释的元素必须在合适的范围内                  | byte、short、int、long及各自的包装类型以及BigDecimal、BigInteger、String |

# 二、开始使用Bean Validation

使用到的依赖

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
    </dependencies>
```

假如有一个文章的实体类，用于接收前端的传来的数据。使用了校验注解`@Size`和`@NotBlank`来校验文章标题是否为空，且长度是都在允许的范围内；使用了校验注解`@Email`来校验是否符合邮箱格式；使用校验注解`@URL`来校验封面是否是一个url。

```java
@Data
public class Article {
    /**
     * 文章id
     */
    private Long articleId;
    /**
     * 文章标题
     */
    @Size(min = 5, max = 10)
    @NotBlank(message = "文章标题不能为空")
    private String title;
    /**
     * 作者
     */
    private String author;
    /**
     * 描述
     */
    private String description;
    /**
     * 内容
     */
    private String context;
    /**
     * 邮箱
     */
    @Email
    private String email;
    /**
     * 封面
     */
    @URL
    private String coverImage;

    /**
     * 状态
     */
    private Integer status;
}
```

编写controller，一个Valid对应一个BindingResult。

```java
@RestController
public class TestController {

    @PostMapping("test")
    public Object test(@Valid @RequestBody Article article, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 这里用一个Map来模拟开发中的一个返回对象
            HashMap<String, Object> map = new HashMap<>(3);

            HashMap<String, String> data = new HashMap<>(16);
            bindingResult.getFieldErrors().stream().forEach(item -> {
                String message = item.getDefaultMessage();
                String field = item.getField();
                data.put(field, message);
            });

            map.put("code", 400);
            map.put("message", "参数不合法");
            map.put("data", data);

            return map;
        }else {
            // 校验成功，继续业务逻辑
            // ....
            return article;
        }
    }
}
```

测试结果：

![](https://cdn.jsdelivr.net/gh/MaiSR9527/blog-pic/record/record-bv1.png)

# 三、统一异常处理和分组校验

## 3.1 使用统一异常处理

如果是按照在上面的示例来写，那么每个方法都要去处理BindingResult这会显得很麻烦和很low，使得代码很冗余。

在Spring Boot中可以使用`ControllerAdvicd`来做统一异常处理。如下：

```java
@RestControllerAdvice
public class CustomExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleValidateException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        // 这里用一个Map来模拟开发中的一个返回对象
        HashMap<String, Object> map = new HashMap<>(3);

        HashMap<String, String> data = new HashMap<>(16);
        bindingResult.getFieldErrors().stream().forEach(item -> {
            String message = item.getDefaultMessage();
            String field = item.getField();
            data.put(field, message);
        });

        map.put("code", 400);
        map.put("message", "参数不合法");
        map.put("data", data);

        return map;
    }
}
```

## 3.2 使用分组校验

可能会有这种情况，文章在添加的时候是不能带Id使用数据库自增Id或者自定义的Id，但是在更新的时候就必须带上Id。又例如在更新文章状态的时候参数是有文章的状态改变了，只需要校验文章状态。所以引入分组校验的概念，来解决。

```java
public class ValidateGroup {
    // 添加时校验
    public static interface AddValidate {
    }
	// 更新时检验
    public static interface UpdateValidate {
    }
	// 更新文章状态是校验
    public static interface ArticleStatusValidate {
    }
}
```

实体类，这些校验注解都有一个groups属性，指定该校验注解在那些分组上生效。

```java
@Data
public class Article {
    /**
     * 文章id
     */
    @NotNull(message = "文章更新时articleId不能为空", groups = {ValidateGroup.UpdateValidate.class})
    @Null(message = "文章新增时articleId必须为空", groups = {ValidateGroup.AddValidate.class})
    private Long articleId;
    /**
     * 文章标题
     */
    @Size(min = 5, max = 10, groups = {ValidateGroup.AddValidate.class, ValidateGroup.UpdateValidate.class})
    @NotBlank(message = "文章标题不能为空", groups = {ValidateGroup.AddValidate.class, ValidateGroup.UpdateValidate.class})
    private String title;
    /**
     * 作者
     */
    private String author;
    /**
     * 描述
     */
    private String description;
    /**
     * 内容
     */
    private String context;
    /**
     * 邮箱
     */
    @Email
    private String email;
    /**
     * 封面
     */
    @URL(groups = {ValidateGroup.AddValidate.class, ValidateGroup.UpdateValidate.class})
    private String coverImage;

    /**
     * 状态
     */
    private Integer status;
}

```

编写几个测试方法

```java
@RestController
public class TestController {

    @PostMapping("add")
    public Object add(@Validated(value = {ValidateGroup.AddValidate.class}) @RequestBody Article article) {
        return article;
    }

    @PostMapping("update")
    public Object update(@Validated(value = {ValidateGroup.UpdateValidate.class}) @RequestBody Article article) {
        return article;
    }

}

```



# 自定义校验器