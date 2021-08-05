package com.msr.better.jpa.service.impl;

import com.msr.better.jpa.dao.StudentRepository;
import com.msr.better.jpa.entity.Student;
import com.msr.better.jpa.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
public class StudentServiceImpl implements IStudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ApplicationContext context;

    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public Student findStudentById(Long id) {
        return studentRepository.getOne(id);
    }

    @Override
    public List<Student> findUsers(String name) {
        return studentRepository.findByNameLike(name);
    }

    @Transactional(rollbackFor = RuntimeException.class, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRES_NEW)
    @Override
    public Student insertStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional(rollbackFor = RuntimeException.class, isolation = Isolation.REPEATABLE_READ)
    @Override
    public void deleteStudent(Long id) {
        IStudentService studentService = context.getBean(IStudentService.class);
        Student db = findStudentById(id);
        if (db == null) {
            throw new RuntimeException("数据不存在");
        }
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }
}
