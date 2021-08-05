package com.msr.better;

import com.msr.better.controller.SysUserController;
import com.msr.better.domain.SysUser;
import com.msr.better.service.ISysUserService;
import com.msr.better.service.ThirdSystemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;

import java.io.File;
import java.io.FileInputStream;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-04 22:15:14
 */
@RunWith(SpringRunner.class)
@WebMvcTest(SysUserController.class)
//@Transactional
public class ServiceUnitTest {

    @MockBean
    private ThirdSystemService thirdSystemService;

//    @Autowired
//    private ISysUserService userService;
//
//    @Test
//    public void test1() {
//        Long expectResult = 100L;
//        given(thirdSystemService.develop()).willReturn(expectResult);
//        SysUser sysUser = userService.findById(expectResult);
//        System.out.println(sysUser.toString());
//    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void test2() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/hello/{id}", 1L)
                .param("name", "hello");

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", "id").value(2L))
                .andExpect(MockMvcResultMatchers.view().name("index.html"))
                .andExpect(MockMvcResultMatchers.model().attribute("id",1L))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void test3() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("");
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", fileInputStream);
        MockMultipartHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/upload").file(mockMultipartFile);
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void test4() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("index.html")
                .sessionAttr("name", "hello")
                .cookie(new Cookie("token", "123345"));
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void test5() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("index.html")
                .content(MediaType.APPLICATION_JSON_VALUE) // 期望返回类型
                .contentType(MediaType.APPLICATION_JSON_VALUE) // 提交的内容类型
                .header("token", 1235); // 设置请求头
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }
}
