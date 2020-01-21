package com.spring.shiro.demo.realm;

import com.spring.shiro.demo.config.JwtToken;
import com.spring.shiro.demo.entity.SUserEntity;
import com.spring.shiro.demo.repository.SUserRepo;
import com.spring.shiro.demo.utils.JwtUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

//import com.spring.shiro.demo.service.SysUserService;

/**
 * @Description: Shiro各个组件说明
 * <p>
 * Realm:
 * <p>
 * realm作用：Shiro 从 Realm 获取安全数据
 * 　　默认自带的realm：idae查看realm继承关系，有默认实现和自定义继承的realm
 * 两个概念
 * 　　principal : 主体的标示，可以有多个，但是需要具有唯一性，常见的有用户名，手机号，邮箱等
 * 　　credential：凭证, 一般就是密码
 * 　　所以一般我们说 principal + credential 就账号 + 密码
 * 开发中，往往是自定义realm , 即继承 AuthorizingRealm,重写doGetAuthenticationInfo(认证方法)和doGetAuthorizationInfo(授权方法)
 * <p>
 * 当用户登陆的时候会调用 doGetAuthenticationInfo
 * 进行权限校验的时候会调用: doGetAuthorizationInfo
 * <p>
 * UsernamePasswordToken ： 对应就是 shiro的token中有Principal和Credential
 * 　　UsernamePasswordToken-》HostAuthenticationToken-》AuthenticationToken
 * SimpleAuthorizationInfo：代表用户角色权限信息
 * SimpleAuthenticationInfo ：代表该用户的认证信息
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/12/4
 */
@Component
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    SUserRepo sUserRepo;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权方法(接口保护，验证接口调用权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        SysUser user = (SysUser)principals.getPrimaryPrincipal();
//        // 用户权限列表，根据用户拥有的权限标识与如 @permission标注的接口对比，决定是否可以调用接口
//        Set<String> permsSet = sysUserService.findPermissions(user.getUsername());
//        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        info.setStringPermissions(permsSet);
        String userId = JwtUtil.getClaim(principals.toString(), "userId");
        Optional<SUserEntity> user = sUserRepo.findById(Long.parseLong(userId));
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        Set<String> permissions = new HashSet<>();
        permissions.add("user:add");
        permissions.add("user:remove");
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        simpleAuthorizationInfo.setStringPermissions(permissions);
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
    }

    /**
     * 认证方法(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        String token = (String) authenticationToken.getPrincipal();
//        // 根据accessToken，查询用户token信息
//        SysUserToken sysUserToken = sysUserTokenService.findByToken(token);
//        if(sysUserToken == null || sysUserToken.getExpireTime().getTime() < System.currentTimeMillis()){
//            // token已经失效
//            throw new IncorrectCredentialsException("token失效，请重新登录");
//        }
//        // 查询用户信息
//        SysUser user = sysUserService.findById(sysUserToken.getUserId());
//        // 账号被锁定
//        if(user.getStatus() == 0){
//            throw new LockedAccountException("账号已被锁定,请联系管理员");
//        }
//        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, token, getName());
//        return null;

//        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
//        SUserEntity user = sUserRepo.findByPhone(token.getUsername());
        String token = (String) authenticationToken.getCredentials();
//        // 解密获得username，用于和数据库进行对比
        String userId = JwtUtil.getClaim(token, "userId");
//        String currentTimeMilli = JwtUtil.getClaim(token, "currentTimeMillis");
//        Date expire = JwtUtil.getExpirationDateFromToken(token);
        if (userId == null) {
            throw new AuthenticationException("token无效");
        }

        Optional<SUserEntity> sUserEntity = sUserRepo.findById(Long.parseLong(userId));
        if (Objects.isNull(sUserEntity)) {
            throw new AuthenticationException("用户不存在!");
        }

        if (!JwtUtil.verify(token, Long.parseLong(userId), sUserEntity.get().getPassword())) {
            throw new AuthenticationException("token已失效");
        }
//         获取盐值，即用户名
//        ByteSource salt = ByteSource.Util.bytes(sUserEntity.get().getPhone());
        //注意，数据库中的user的密码必须是要经过md5加密，不然还是会抛出异常！！！！！
//      return new SimpleAuthenticationInfo(token, sUserEntity.get().getPassword(), "shiro_realm");
        return new SimpleAuthenticationInfo(token, token, "shiro_realm");
    }
}
