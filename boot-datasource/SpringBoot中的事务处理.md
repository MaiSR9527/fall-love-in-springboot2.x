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

# 三、数据库事务隔离级别

## 3.1 数据库事务的基本特征

* Atomicity（原子性）：一个事务（transaction）中的所有操作，要么全部完成，要么全部不完成，不会结束在中间某个环节。事务在执行过程中发生错误，会被恢复（Rollback）到事务开始前的状态，就像这个事务从来没有执行过一样。
* Consistency（一致性）：在事务开始之前和事务结束以后，数据库的完整性没有被破坏。这表示写入的资料必须完全符合所有的预设规则，这包含资料的精确度、串联性以及后续数据库可以自发性地完成预定的工作。
* Isolation（隔离性）：数据库允许多个并发事务同时对其数据进行读写和修改的能力，隔离性可以防止多个事务并发执行时由于交叉执行而导致数据的不一致。事务隔离分为不同级别，包括读未提交（Read uncommitted）、读提交（read committed）、可重复读（repeatable read）和串行化（Serializable）。这也是本节要讲的内容。
* Durability（持久性）：事务处理结束后，对数据的修改就是永久的，即便系统故障也不会丢失。 

这里引用一下网上用的很多的讲述隔离性的例子。假设有一种商品有库存100，每次都只能抢购一件商品。就会出现各种情况：

<center>第一类丢失更新</center>

| 时刻 | 事务1                   | 事务2                  |
| ---- | ----------------------- | ---------------------- |
| T1   | 初始库存100             | 初始库存100            |
| T2   | 扣减库存，剩余99        |                        |
| T3   |                         | 扣减库存，剩余99       |
| T4   |                         | 提交事务，商品库存为99 |
| T5   | 回滚事务，商品库存为100 |                        |

像上面所述，对于一个事务回滚了另外一个事务提交，而引发的数据不一致的情况，被称为第一类丢失更新。目前的数据库已经解决了第一类丢失更新的问题，即上述表中描述的问题。

<center>第二类丢失更新</center>

| 时刻 | 事务1                | 事务2                |
| ---- | -------------------- | -------------------- |
| T1   | 初始库存100          | 初始库存100          |
| T2   | 扣减库存，剩余99     |                      |
| T3   |                      | 扣减库存，剩余99     |
| T4   |                      | 提交事务，剩余库存99 |
| T5   | 提交事务，剩余库存99 |                      |

在事务1中无法感知事务2的操作，所以事务1并不知道事务2修改过数据，因此它认为只是发生了一个业务，所以库存还是被事务1修改成99。T5时刻事务1的提交，导致事务2提交结果丢失，这样多个事务都提交引发的数据丢失更新称为第二类丢失更新。这是互联网系统重点关注的内容，为了克服这个问题，数据库提出了事务之间的隔离级别的概念。

## 3.2 详解数据库隔离级别

为了压制更新丢失，数据库标准踢输了四种隔离级别，在不同程度上压制丢失更新。这四类隔离级别就是上面提到的：未提交读，读写提交，可重复读和串行化，它们会在不同程度上压制丢失更新的情况。

### 3.2.1 未提交读

未提交读是数据库最低的隔离级别，它允许一个事务读取另外一个事务没有提交的数据。未提交读是很危险的隔离级别，一般在实际开发中并没有广泛使用，它的最大的有点就是并发能力高，适合一些对数据一致性没有要求但追求高并发的场景，最大的缺点就是会造成**脏读**。

<center>脏读现象</center>

| 时刻 | 事务1       | 事务2    | 备注                                                         |
| ---- | ----------- | -------- | ------------------------------------------------------------ |
| T0   |             |          | 商品的库存初始化数量为100                                    |
| T1   | 读取库存100 |          |                                                              |
| T2   | 扣减库存    |          | 此时库存为99                                                 |
| T3   |             | 扣减库存 | 此时库存为98，读取到了事务1没提交的数据                      |
| T4   |             | 提交事务 | 库存保存为98                                                 |
| T5   | 回滚事务    |          | 因为第一类丢失更新已经解决，所以库存不会回滚到100，此时库存为98 |

如果数据使用了未提交读的隔离级别，就可能出现上述表格中的问题。这种现象被称为脏读，事务2读取到了事务1还没提交的数据，当事务1回滚之后，这数据便成为了脏数据。在读写提交的隔离级别中就克服了脏读的现象。

### 3.2.2 读提交

读写提交隔离级别，是指一个事务只能读取到另外一个事务已经提交的数据，不能读取未提交的数据。

| 时刻 | 事务1       | 事务2    | 备注                                                         |
| ---- | ----------- | -------- | ------------------------------------------------------------ |
| T0   |             |          | 商品的库存初始化数量为100                                    |
| T1   | 读取库存100 |          |                                                              |
| T2   | 扣减库存    |          | 此时库存为99                                                 |
| T3   |             | 扣减库存 | 此时库存为99，读取不了事务1没提交的数据                      |
| T4   |             | 提交事务 | 库存保存为99                                                 |
| T5   | 回滚事务    |          | 因为第一类丢失更新已经解决，所以库存不会回滚到100，此时库存为99 |

这就是读提交，若有事务对数据进行更新操作时，读操作事务要等待这个更新操作事务提交后才能读取数据，可以解决脏读问题。但在这个事例中，如果事务2先不提交，事务1未提交是查询的库存是99，事务1提交了事务2再去查询库存此时库存是98，这就出现了一个事务范围内两个相同的查询却返回了不同数据，这就是**不可重复读**。

 这是各种系统中最常用的一种隔离级别，也是SQL Server和Oracle的默认隔离级别。这种隔离级别能够有效的避免脏读，但除非在查询中显示的加锁，如：

```sql
select * from T where ID=2 lock in share mode;
select * from T where ID=2 for update;
```

很明显读提交隔离级别会引起**不可重复读**现象，而可重复读隔离级别就可以解决**不可重复读**。

### 3.2.3 可重复读

可重复读的目标就是克服读提交中出现的不可重复读的现象，因为在读提交的时候，可能出现一些值的变化，影响当前事务的执行。

<center>解决不可重复度</center>

| 时刻 | 事务1       | 事务2    | 备注                      |
| ---- | ----------- | -------- | ------------------------- |
| T0   |             |          | 商品的库存初始化数量为100 |
| T1   | 读取库存100 |          |                           |
| T2   | 扣减库存    |          | 此时库存为99              |
| T3   |             | 读取库存 | 不允许读取，事务1还没提交 |
| T4   | 提交事务    |          | 库存保存为99              |
| T5   |             | 读取库存 | 此时库存为99              |

<center>幻读</center>

| 时刻 | 事务1            | 事务2        | 备注                                                         |
| ---- | ---------------- | ------------ | ------------------------------------------------------------ |
| T0   |                  |              | 商品的库存初始化数量为100                                    |
| T1   | 读取库存100      |              |                                                              |
| T2   |                  | 查询订单记录 | 0笔订单记录                                                  |
| T3   | 扣库存           |              | 库存保存为99                                                 |
| T4   | 插入一个订单记录 |              | 新增1条订单记录                                              |
| T5   | 提交事务         |              | 此时库存为99，1条订单记录                                    |
| T6   |                  | 查询订单记录 | 有1条订单记录，出现了查询不一致，在事务2看来出现了和之前查询不一致的结果 |

### 3.2.4 串行化

串行化是数据库最高的隔离级别，它要求所有的sql按照顺序执行，这样就可以克服上面所述的所有问题，所以能够保证数据的一致性。但是性能也是最差的。

### 3.2.5 各个隔离级别的总结

| 隔离级别 | 脏读 | 不可重复读 | 欢度 |
| -------- | ---- | ---------- | ---- |
| 未提交读 | √    | √          | √    |
| 读提交   | ×    | √          | √    |
| 可重复读 | ×    | ×          | √    |
| 串行化   | ×    | ×          | ×    |

对于不同的数据库的支持是不一样的，Oracle只能支持读提交和串行化，MySQL则是能够支持上面四种。Oracle默认的隔离级别是读提交，MySQL则是可重复读。

# 四、数据库事务传播行为

传播行为是方法之间调用事务采取的策略问题。在通常情况下数据库事务要么全部成功，要么全部失败。在Spring中当一个方法调用另外一个方法时，可以让事务采取不同的策略，例如新建一个事务处理或者挂起当前事务等，这就是事务的传播。例如下面`deleteStudent()`调用了`findStudentById(id)`检查Student是否存在。

```java
@Service
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Student findStudentById(Long id) {
        return studentRepository.getOne(id);
    }

    ...

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void deleteStudent(Long id) {
        Student student = findStudentById(id);
        if(student == null){
            // data not exists
        }
        studentRepository.deleteById(id);
    }
    
    ...
}

```

在Spring中支持的事务传播行为：

```java
package org.springframework.transaction.annotation;

import org.springframework.transaction.TransactionDefinition;

public enum Propagation {

	/**
	 * 支持当前事务，如果不存在则创建一个新事务。 类似于同名的 EJB 事务属性。
	 * 这是事务注释的默认设置。
	 */
	REQUIRED(TransactionDefinition.PROPAGATION_REQUIRED),

	/**
	 * 支持当前事务，如果不存在则以非事务方式执行。 类似于同名的 EJB 事务属性。
	 * 注意：对于具有事务同步的事务管理器， SUPPORTS与根本没有事务略有不同，因为它定义了同步将应用的事务范围。
     * 因此，相同的资源（JDBC 连接、Hibernate 会话等）将在整个指定范围内共享。 请注意，这取决于事务管理器的实际同步配置
	 */
	SUPPORTS(TransactionDefinition.PROPAGATION_SUPPORTS),

	/**
	 * 支持当前事务，如果不存在则抛出异常。 类似于同名的 EJB 事务属性。
	 */
	MANDATORY(TransactionDefinition.PROPAGATION_MANDATORY),

	
	/**
	 * 创建一个新事务，如果存在，则暂停当前事务。 类似于同名的 EJB 事务属性。
	 * 注意：实际的事务暂停不会在所有事务管理器上开箱即用。 
	 * 这尤其适用于org.springframework.transaction.jta.JtaTransactionManager 
	 * 它需要javax.transaction.TransactionManager对其可用（这在标准 Java EE 中是特定于服务器的）
	 */
	REQUIRES_NEW(TransactionDefinition.PROPAGATION_REQUIRES_NEW),

	/**
	 * 以非事务方式执行，如果存在则暂停当前事务。 类似于同名的 EJB 事务属性。
	 * 注意：实际的事务暂停不会在所有事务管理器上开箱即用。 这尤其适用于org.springframework.transaction.jta.JtaTransactionManager 
	 * 它需要javax.transaction.TransactionManager对其可用（这在标准 Java EE 中是特定于服务器的）。
	 */
	NOT_SUPPORTED(TransactionDefinition.PROPAGATION_NOT_SUPPORTED),

	/**
	 * 以非事务方式执行，如果存在事务则抛出异常。 类似于同名的 EJB 事务属性。
	 */
	NEVER(TransactionDefinition.PROPAGATION_NEVER),

	/**
	 * 如果当前事务存在，则在嵌套事务中执行，否则行为类似于REQUIRED 。 EJB 中没有类似的特性。
	 * 注意：嵌套事务的实际创建仅适用于特定的事务管理器。 开箱即用，
	 * 这仅适用于 JDBC DataSourceTransactionManager。 一些 JTA 提供者也可能支持嵌套事务。
	 * @see org.springframework.jdbc.datasource.DataSourceTransactionManager
	 */
	NESTED(TransactionDefinition.PROPAGATION_NESTED);


	private final int value;


	Propagation(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

}
```

事务的传播行为，默认值为 Propagation.REQUIRED。可以手动指定其他的事务传播行为，如下：
**（1）Propagation.REQUIRED**

如果当前存在事务，则加入该事务，如果当前不存在事务，则创建一个新的事务。
**（2）Propagation.SUPPORTS**

如果当前存在事务，则加入该事务；如果当前不存在事务，则以非事务的方式继续运行。
**（3）Propagation.MANDATORY**

如果当前存在事务，则加入该事务；如果当前不存在事务，则抛出异常。
**（4）Propagation.REQUIRES_NEW**

重新创建一个新的事务，如果当前存在事务，延缓当前的事务。
**（5）Propagation.NOT_SUPPORTED**

以非事务的方式运行，如果当前存在事务，暂停当前的事务。
**（6）Propagation.NEVER**

以非事务的方式运行，如果当前存在事务，则抛出异常。
**（7）Propagation.NESTED**

如果没有，就新建一个事务；如果有，就在当前事务中嵌套其他事务。

# 五、Spring中的声明式事务的使用

TODO