package com.spring.shiro.demo.service;

import com.spring.shiro.demo.entity.UserToken;

public interface TokenService {

    UserToken createToken(Long userId);
}
