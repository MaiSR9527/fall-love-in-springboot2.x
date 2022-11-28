package com.msr.better.jpa.dao;

import com.msr.better.jpa.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022-11-28 23:26
 **/
@Repository
public interface ICourseRepository extends JpaRepository<Course, Long> {
}
