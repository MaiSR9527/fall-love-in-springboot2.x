package com.msr.better;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-04 00:28:17
 */
@SpringBootTest(classes = {TestDeployApplication.class})
public class UnitTest1 {

    @BeforeEach
    public void beforeClass() {
        System.out.println("=================BeforeClass================");
    }

    @AfterEach
    public void afterClass() {
        System.out.println("=================AfterClass================");
    }

    @BeforeAll
    public static void beforeTest1() {
        System.out.println("before test 1");
    }
    @BeforeAll
    public static void beforeTest2() {
        System.out.println("before test 2");
    }

    @AfterAll
    public static void afterTest() {
        System.out.println("after test");
    }

    @Test
    public void test1() {
        System.out.println("test1");
    }

    @Test
    public void test2() {
        System.out.println("test2");
    }
}
