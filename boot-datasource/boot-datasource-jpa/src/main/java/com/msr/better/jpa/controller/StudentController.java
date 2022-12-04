package com.msr.better.jpa.controller;

import com.msr.better.jpa.entity.Student;
import com.msr.better.jpa.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022-12-04 23:37
 **/
@RestController
@RequestMapping("stu")
public class StudentController {

    @Autowired
    private IStudentService studentService;

    @GetMapping("/{id}")
    public Object getStuById(@PathVariable("id") long id) {
        Student student = studentService.findStudentById(id);
        return student;
    }

}
