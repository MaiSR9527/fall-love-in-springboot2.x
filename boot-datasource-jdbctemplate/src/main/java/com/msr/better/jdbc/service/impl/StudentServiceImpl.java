package com.msr.better.jdbc.service.impl;

import com.msr.better.jdbc.domain.Student;
import com.msr.better.jdbc.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-30 00:00
 **/
@SuppressWarnings("All")
@Service
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
}
