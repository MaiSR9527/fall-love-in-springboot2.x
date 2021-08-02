---
layout: springboot
title: SpringBoot中的事务处理
date: 2021-05-20 9:10:12
categories: java
tags: SpringBoot
---

# 一、前言

在Spring中，数据库事务是通过AOP技术来提供服务的。使用原生的JDBC操作时会存在大量的`try{}catch{}finally{}`语句，所以会存在大量的冗余代码，例如打开和关闭数据库连接和数据库事务回滚等。通过Spring的AOP之后，这些冗余的代码就都被处理了。

# 二、回顾JDBC的数据库事务

接下来我们一起回顾一下，刚入门使用JDBC操作的时候，写得让人烦躁代码片段吧。

```java
@Service
public class JdbcTransaction {
    @Autowired
    private DataSource dataSource;

    public int insertStudent(Student student) {
        Connection connection = null;
        int result = 0;
        try {
            // 获取数据连接
            connection = dataSource.getConnection();
            // 开始事务
            connection.setAutoCommit(false);
            // 设置隔离级别
            connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            // 执行sql
            PreparedStatement prepareStatement = connection.prepareStatement("insert into t_student(name,gender,age) values (?,?,?)");
            prepareStatement.setString(1, student.getName());
            prepareStatement.setString(2, student.getGender());
            prepareStatement.setInt(3, student.getAge());
            result = prepareStatement.executeUpdate();
            // 提交事务
            connection.commit();
        } catch (Exception e1) {
            if (connection != null) {
                try {
                    // 事务回滚
                    connection.rollback();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            e1.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    // 关闭连接
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
```

在上述的一大串代码中也就一下已行的业务代码是我们最为关注的，在每个使用JDBC的业务代码中，都经常可以看到数据库连接的获取和关闭以及事务的提交和回滚，大量的try...catch...finally..语句会充斥在代码块中。而我们也仅是想执行简单的一条sql代码而已。如果执行多条sql，这些代码显然更加的就难以控制。

> PreparedStatement prepareStatement = connection.prepareStatement("insert into t_student(name,gender,age) values (?,?,?)");
> prepareStatement.setString(1, student.getName());
> prepareStatement.setString(2, student.getGender());
> prepareStatement.setInt(3, student.getAge());
> result = prepareStatement.executeUpdate();

随着不断地的发展和优化，使用像MyBatis或Hibernate这种ORM框架可以减少一些代码，但是依旧不能减少打开和关闭数据库连接和事务控制的代码，但是这些我们可以通过AOP把这些公共代码抽取出来，单独实现。