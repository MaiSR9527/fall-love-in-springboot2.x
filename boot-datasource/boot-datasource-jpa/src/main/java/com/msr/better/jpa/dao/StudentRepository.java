package com.msr.better.jpa.dao;

import com.msr.better.jpa.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-02 00:31:35
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    /**
     * 命名查询：通过name属性模糊查询
     *
     * @param name 学生名字
     * @return 查询结果
     */
    List<Student> findByNameLike(String name);

    /**
     * jql语言查询
     * @param name
     * @return
     */
    @Query("from Student where name like concat('%',?1,'%')")
    List<Student> getUsers(String name);

}
