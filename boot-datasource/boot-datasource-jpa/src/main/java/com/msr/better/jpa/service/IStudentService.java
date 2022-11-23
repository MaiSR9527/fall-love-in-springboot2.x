package com.msr.better.jpa.service;

import com.msr.better.jpa.entity.Student;

import java.util.List;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-02 00:31:35
 */
public interface IStudentService {

    Student findStudentById(Long id);

    List<Student> findUsers(String name);

    Student insertStudent(Student student);

    Student updateStudent(Student student);

    void deleteStudent(Long id);

    List<Student> findAll();

    int saveBatchTest();
}
