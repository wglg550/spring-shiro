package com.spring.shiro.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class UserToken implements Serializable {
    private Long userId;
    private String token;
    private Date lastUpdateTime;
    private Long expire;
    private Date expireTime;
}
