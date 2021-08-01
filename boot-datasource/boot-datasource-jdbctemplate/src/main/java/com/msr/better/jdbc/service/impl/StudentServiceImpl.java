package com.msr.better.jdbc.service.impl;

import com.msr.better.jdbc.domain.Student;
import com.msr.better.jdbc.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.StatementCallback;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-30 00:00
 **/
@SuppressWarnings("All")
@Service
@Slf4j
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 拿到映射关系
     *
     * @return
     */
    private RowMapper<Student> getStudentMapper() {
        return (resultSet, i) -> {
            Student student = new Student();
            student.setId(resultSet.getLong("id"));
            student.setName(resultSet.getString("name"));
            student.setAge(resultSet.getInt("age"));
            student.setGender(resultSet.getString("gender"));
            return student;
        };
    }

    @Override
    public Student findStudentById(Long id) {
        String sql = "select id,name,gender,age from t_student where id = ?";
        Object[] param = new Object[]{id};

        return jdbcTemplate.queryForObject(sql, param, getStudentMapper());

    }

    @Override
    public List<Student> findUsers(String name) {
        String sql = "select id,name,gender,age from t_student " +
                "where name like concat('%',?,'%')";
        Object[] param = new Object[]{name};
        return jdbcTemplate.query(sql, param, getStudentMapper());
    }

    @Override
    public int insertStudent(Student student) {
        String sql = "insert into t_student(name,gender,age) value(?,?,?)";

        return jdbcTemplate.update(sql, student.getName(), student.getGender(), student.getAge());
    }

    @Override
    public int updateStudent(Student student) {
        String sql = "update t_student set name=?,gender=?,age=? where id=?";
        return jdbcTemplate.update(sql, student.getName(), student.getGender(), student.getAge(), student.getId());
    }

    @Override
    public int deleteStudent(Long id) {
        String sql = "delete from t_student where id = ?";
        return jdbcTemplate.update(sql, id);
    }

    @Override
    public List<Student> findAll() {
        String sql = "select * from t_student";
        return jdbcTemplate.query(sql, getStudentMapper());
    }

    public Student findStudentById2(Long id) {
        return jdbcTemplate.execute(new StatementCallback<Student>() {
            @Override
            public Student doInStatement(Statement statement) throws SQLException, DataAccessException {
                String sql1 = "select count(*) total from t_student where id = " + id;
                ResultSet resultSet1 = statement.executeQuery(sql1);
                while (resultSet1.next()) {
                    int total = resultSet1.getInt("total");
                    log.info("total：{}", total);
                }
                String sql2 = "select id,name,gender,age from t_student where id = " + id;
                ResultSet resultSet2 = statement.executeQuery(sql2);
                Student student = new Student();
                while (resultSet2.next()) {
                    int row = resultSet2.getRow();
                    student = getStudentMapper().mapRow(resultSet2, row);
                }
                return student;
            }
        });
    }

    public Student findStudentById3(Long id) {
        return jdbcTemplate.execute(new ConnectionCallback<Student>() {
            @Override
            public Student doInConnection(Connection connection) throws SQLException, DataAccessException {
                String sql1 = "select count(*) total from t_student where id = ?";
                PreparedStatement statement1 = connection.prepareStatement(sql1);
                statement1.setLong(1, id);
                ResultSet resultSet1 = statement1.executeQuery();
                while (resultSet1.next()) {
                    int total = resultSet1.getInt("total");
                    log.info("total：{}", total);
                }

                String sql2 = "select id,name,gender,age from t_student where id = ?";
                PreparedStatement statement2 = connection.prepareStatement(sql2);
                statement2.setLong(1, id);
                ResultSet resultSet2 = statement2.executeQuery();
                Student student = new Student();
                while (resultSet2.next()) {
                    int row = resultSet2.getRow();
                    student = getStudentMapper().mapRow(resultSet2, row);
                }
                return student;
            }
        });
    }

}
