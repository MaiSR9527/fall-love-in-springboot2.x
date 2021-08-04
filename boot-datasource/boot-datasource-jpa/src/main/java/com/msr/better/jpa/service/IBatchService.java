package com.msr.better.jpa.service;

import com.msr.better.jpa.domain.Student;

import java.util.List;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-02 00:31:35
 */
public interface IBatchService {

    void batchInsertStudent(List<Student> students);
}
