package com.spring.shiro.demo.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserDomain {
    private Long userId;
    private String userName;
    private String phone;
    private String token;
    private String exipre;
    private Set<String> roles;
    private Set<String> perms;

    public UserDomain() {

    }

    public UserDomain(Long userId, String userName, String phone, String token) {
        this.userId = userId;
        this.userName = userName;
        this.phone = phone;
        this.token = token;
    }

}
