package com.msr.better.jpa.service.impl;

import com.msr.better.jpa.dao.StudentRepository;
import com.msr.better.jpa.domain.Student;
import com.msr.better.jpa.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Override
    public Student findStudentById(Long id) {
        return studentRepository.getOne(id);
    }

    @Override
    public List<Student> findUsers(String name) {
        return studentRepository.findByNameLike(name);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Student insertStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }
}
