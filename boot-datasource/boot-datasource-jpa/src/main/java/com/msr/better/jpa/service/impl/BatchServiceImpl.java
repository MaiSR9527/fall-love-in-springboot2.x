package com.msr.better.jpa.service.impl;

import com.msr.better.jpa.entity.Student;
import com.msr.better.jpa.service.IBatchService;
import com.msr.better.jpa.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-02 00:31:35
 */
@Service
public class BatchServiceImpl implements IBatchService {

    @Autowired
    private IStudentService studentService;

    @Transactional(rollbackFor = RuntimeException.class, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    @Override
    public void batchInsertStudent(List<Student> students) {
        for (Student student : students) {
            studentService.insertStudent(student);
        }
    }
}
