package com.spring.shiro.demo.service.impl;

import com.spring.shiro.demo.entity.UserToken;
import com.spring.shiro.demo.service.TokenService;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public UserToken createToken(Long userId) {
        return null;
    }
}
