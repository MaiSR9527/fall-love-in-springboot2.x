package com.msr.better.service.impl;

import com.msr.better.domain.SysUser;
import com.msr.better.service.ITestService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-04 00:30:29
 */
@Service
public class TestServiceImpl implements ITestService {
    @Override
    public List<SysUser> listAll() {
        List<SysUser> res = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SysUser sysUser = new SysUser();
            sysUser.setAge(19 + i);
            sysUser.setId((long) i);
            res.add(sysUser);
        }
        return res;
    }
}
