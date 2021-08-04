package com.msr.better;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-04 00:28:17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UnitTest1 {

    @BeforeClass
    public static void beforeClass() {
        System.out.println("=================BeforeClass================");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("=================AfterClass================");
    }

    @Before
    public void beforeTest() {
        System.out.println("before test");
    }

    @After
    public void afterTest() {
        System.out.println("after test");
    }

    @Test
    public void test1() {
        System.out.println("test1");
    }

    @Test
    public void test2() {
        System.out.println("test2") ;
    }


}
