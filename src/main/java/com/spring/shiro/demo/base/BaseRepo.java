package com.spring.shiro.demo.base;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @Description: 基础repo
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/12/6
 */
@NoRepositoryBean
public interface BaseRepo<T> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
}
