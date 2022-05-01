package com.msr.better.jdbc;

import com.msr.better.jdbc.domain.Student;
import com.msr.better.jdbc.service.IStudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-30 00:00
 **/
@SpringBootTest
public class TestApplication {

    @Autowired
    private IStudentService studentService;

    @Test
    public void test1() {
        Student student = new Student();
        student.setName("tom");
        student.setGender("男");
        student.setAge(18);
        studentService.insertStudent(student);
        student.setAge(20);
        studentService.insertStudent(student);
        student.setAge(22);
        studentService.insertStudent(student);
    }

    @Test
    public void test2() {
        Student student = studentService.findStudentById(1L);
        System.out.println(student.toString());
        List<Student> all = studentService.findAll();
        all.forEach(e -> System.out.println(e.toString()));
    }

    @Test
    public void test3() {
        Student student = new Student();
        student.setId(1L);
        student.setName("jack");
        student.setGender("男");
        student.setAge(18);
        studentService.updateStudent(student);
        Student db = studentService.findStudentById(1L);
        System.out.println(db.toString());

    }

    @Test
    public void test4() {
        List<Student> tom = studentService.findUsers("tom");
        tom.forEach(e -> System.out.println(e.toString()));
    }

    @Test
    public void test5() {
        studentService.deleteStudent(3L);
        List<Student> all = studentService.findAll();
        all.forEach(student -> System.out.println(student.toString()));
    }

}
