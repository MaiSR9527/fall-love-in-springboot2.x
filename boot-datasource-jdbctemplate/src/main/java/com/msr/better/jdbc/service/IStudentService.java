package com.msr.better.jdbc.service;

import com.msr.better.jdbc.domain.Student;

import java.util.List;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-30 00:00
 **/
public interface IStudentService {

    Student findStudentById(Long id);

    List<Student> findUsers(String name);

    int insertStudent(Student student);

    int updateStudent(Student student);

    int deleteStudent(Long id);
}
