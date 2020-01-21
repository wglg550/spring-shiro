package com.spring.shiro.demo.controller;

import com.spring.shiro.demo.domain.UserDomain;
import com.spring.shiro.demo.entity.SUserEntity;
import com.spring.shiro.demo.http.HttpResult;
import com.spring.shiro.demo.repository.SUserRepo;
import com.spring.shiro.demo.utils.JwtUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Api(value = "test")
@Slf4j
@RestController
@RequestMapping("/")
public class SysLoginController {

    @Autowired
    SUserRepo sUserRepo;

    /**
     * 登录接口
     */
    @PostMapping(value = "/sys/login")
    public HttpResult login(@RequestBody SUserEntity sUserEntity, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        Subject subject = SecurityUtils.getSubject();
        String phone = sUserEntity.getPhone();
        String password = sUserEntity.getPassword();
//        UsernamePasswordToken token = new UsernamePasswordToken(phone, password);
//        subject.login(token);

        // 用户信息
        SUserEntity user = sUserRepo.findByPhone(phone);

        // 账号不存在、密码错误
        if (sUserEntity == null) {
            return HttpResult.error("账号不存在");
        }

//        if (!Objects.equals(password, user.getPassword())) {
//            return HttpResult.error("密码不正确");
//        }

        //账号锁定
//        if (user.getStatus() == 0) {
//            return HttpResult.error("账号已被锁定,请联系管理员");
//        }

        String token = JwtUtil.sign(user.getId(), user.getPassword());
//        String hex = new SimpleHash("MD5", user.getPassword(), user.getPhone(), 2).toHex();
//        log.info("hex:{}", hex);
        UserDomain userDomain = new UserDomain(user.getId(), user.getName(), user.getPhone(), token);
//        Set<String> roles = new HashSet<>();
//        roles.add("admin");
//        roles.add("user");
//        userDomain.setRoles(roles);
        response.setHeader("Authorization", token);
        return HttpResult.ok(userDomain);
    }

    /**
     * 验证用户密码
     *
     * @return
     */
    @PostMapping(value = "/sys/match")
//    @RequiresPermissions({"user:add", "user:remove"})
//    @RequiresAuthentication//使用该注解标注的类，实例，方法在访问或调用时，当前Subject必须在当前session中已经过认证。
    //属于user角色
//    @RequiresRoles("admin")
//必须同时属于user和admin角色
//    @RequiresRoles({"user","admin"})
//属于user或者admin之一;修改logical为OR 即可
//    @RequiresRoles(value={"user","admin"},logical=Logical.OR)
    public boolean match() {
        return true;
//        return user.getPassword().equals(PasswordUtils.encrypte(password, user.getSalt()));
    }
}
