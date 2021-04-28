package com.msr.better.mvc.controller;

import com.msr.better.mvc.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-04-28 23:37
 **/
@Controller
@RequestMapping("user")
public class UserController {

    @RequestMapping("details")
    public ModelAndView userDetails(Long id) {
        // 假设是访问service拿到数据
        User user = new User();
        user.setId(id);
        user.setUsername("zhangsan");
        user.setPassword("123456");
        // 模型与视图
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/details");
        modelAndView.addObject("user", user);
        return modelAndView;
    }
}
