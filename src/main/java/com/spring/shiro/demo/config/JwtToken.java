package com.spring.shiro.demo.config;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Description: jwt token
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/12/6
 */
public class JwtToken implements AuthenticationToken
//        ,RememberMeAuthenticationToken, HostAuthenticationToken
{
    private static final long serialVersionUID = 1L;

    private String token;
    private boolean rememberMe = true;
    private String host = "localhost";

    public JwtToken(String token) {
        this.token = token;
//        this.rememberMe = rememberMe;
//        this.host = host;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

//    @Override
//    public boolean isRememberMe() {
//        return rememberMe;
//    }
//
//    @Override
//    public String getHost() {
//        return host;
//    }
}