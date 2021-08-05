package com.msr.better.service.impl;

import com.msr.better.dao.SysUserRepository;
import com.msr.better.domain.SysUser;
import com.msr.better.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-08-04 00:30:29
 */
@Service
public class SysUserServiceImpl implements ISysUserService {

    @Autowired
    private SysUserRepository userRepository;

    @Override
    public List<SysUser> listAll() {
        return userRepository.findAll();
    }

    @Override
    public SysUser insert(SysUser user) {
        return userRepository.save(user);
    }

    @Override
    public SysUser findById(Long id) {
        return userRepository.getOne(id);
    }


}
