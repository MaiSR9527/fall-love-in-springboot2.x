package com.msr.better.jdbc.test;

import com.msr.better.jdbc.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-30 00:00
 **/
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
                    connection.rollback();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            e1.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
