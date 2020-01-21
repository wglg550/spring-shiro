package com.spring.shiro.demo.repository;

import com.spring.shiro.demo.base.BaseRepo;
import com.spring.shiro.demo.entity.SUserEntity;
import org.springframework.stereotype.Repository;

/**
 * @Description: s_user repo
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/10/11
 */
@Repository
public interface SUserRepo extends BaseRepo<SUserEntity> {

    /**
     * 根据手机号码和密码查询登录用户
     *
     * @param phone
     * @param password
     * @return
     */
    SUserEntity findByPhoneAndPassword(String phone, String password);

    /**
     * 根据手机号码查询登录用户
     *
     * @param phone
     * @return
     */
    SUserEntity findByPhone(String phone);

    /**
     * 根据姓名查询登录用户
     *
     * @param name
     * @return
     */
    SUserEntity findByName(String name);
}
