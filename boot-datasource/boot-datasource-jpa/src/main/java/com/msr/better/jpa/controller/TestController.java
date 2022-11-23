package com.msr.better.jpa.controller;

import com.msr.better.jpa.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2022-11-23 14:34
 **/
@RestController
@RequestMapping("test")
public class TestController {

    @Autowired
    private IStudentService studentService;

    @GetMapping("/batchSave")
    public Object batchSave() {
        return studentService.saveBatchTest();
    }
}
