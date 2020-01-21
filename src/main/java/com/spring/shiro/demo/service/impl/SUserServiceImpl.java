package com.spring.shiro.demo.service.impl;

import com.spring.shiro.demo.repository.SUserRepo;
import com.spring.shiro.demo.service.SUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: s_user serviceImpl
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/10/11
 */
@Service
public class SUserServiceImpl implements SUserService {
    @Autowired
    SUserRepo sUserRepo;

    @Override
    public int save(Object record) {
        return 0;
    }

    @Override
    public int delete(Object record) {
        return 0;
    }

    @Override
    public int delete(List records) {
        return 0;
    }

    @Override
    public Object findById(Long id) {
        return sUserRepo.findById(id);
    }
}
