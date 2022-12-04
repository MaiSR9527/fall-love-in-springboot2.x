package com.msr.better.jpa.service.impl;

import com.msr.better.jpa.constants.GenderEnum;
import com.msr.better.jpa.controller.TestController;
import com.msr.better.jpa.dao.ICourseRepository;
import com.msr.better.jpa.dao.IScoreRepository;
import com.msr.better.jpa.dao.StudentRepository;
import com.msr.better.jpa.entity.Course;
import com.msr.better.jpa.entity.Score;
import com.msr.better.jpa.entity.Student;
import com.msr.better.jpa.service.IStudentService;
import com.msr.better.jpa.util.ChineseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    private ICourseRepository courseRepository;

    @Autowired
    private IScoreRepository scoreRepository;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private TestController testController;

    @Transactional(rollbackFor = RuntimeException.class, propagation = Propagation.REQUIRES_NEW)
    @Override
    public Student findStudentById(Long id) {
        testController.test();
        return studentRepository.getById(id);
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

    @Override
    public int saveBatchTest() {
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < 500 * 100 + 56245; i++) {
            Student student = new Student();

            student.setName(ChineseUtil.getRandomChineseName());
            GenderEnum genderEnum = i % 3 == 0 ? GenderEnum.FEMALE : GenderEnum.MALE;
            student.setGender(genderEnum);
            int age = (int) ((100 + 1) * Math.random()) - (int) (100 * Math.random());
            student.setAge(age);
            String edu = ChineseUtil.getRandomChineseLastName(true) +
                    ChineseUtil.getRandomChineseLastName(false) + ChineseUtil.getRandomChineseChar();
            student.setEducation(edu);
            student.setStatus(i % 2);
            student.setEnableStatus(1);
            student.setPosition("engineer " + i);
            student.setIdCardNumber(UUID.randomUUID().toString());
            long num = (int) ((100 + 1) * Math.random()) - (int) (100 * Math.random());
            student.setCreateTime(new Date(System.currentTimeMillis() - num * 1000));
            student.setCreator("admin");
            student.setUpdateTime(new Date());
            student.setModifier("root");
            student.setNickName(ChineseUtil.getRandomChineseName());
            student.setIconPath("https://www.maishuren.top/upload/2021/04/newavatar-95ae35ecd58c4ca9ba8427df91afe443.jpg");
            list.add(student);
        }
        return studentRepository.saveAllAndFlush(list).size();
    }

    @Override
    public Object saveRelation() {
        List<Course> courseList = new ArrayList<>(500);
        for (int i = 1; i <= 500; i++) {
            Course course = new Course();
            course.setCourseName("课程" + i);
            course.setCreateTime(new Date(System.currentTimeMillis() - (int) (100 * Math.random())));
            courseList.add(course);
        }
        courseRepository.saveAllAndFlush(courseList);

        List<Score> scoreList = new ArrayList<>(500);
        for (int i = 0; i < 500; i++) {
            long randomStuId = (long) (500 * 10000 * Math.random());
            long randomCourseId = (long) (500 * Math.random());
            int randomScore = (int) (100 * Math.random());
            Score score = new Score();
            score.setScore(randomScore);
            score.setStuId(randomStuId);
            score.setCourseId(randomCourseId);
            score.setCreateTime(new Date());
            scoreList.add(score);
        }

        scoreRepository.saveAllAndFlush(scoreList);

        return "success";
    }


}
