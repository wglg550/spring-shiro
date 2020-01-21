package com.spring.shiro.demo.config;

import com.spring.shiro.demo.filter.CustomPermissionsAuthorizationFilter;
import com.spring.shiro.demo.filter.CustomRolesAuthorizationFilter;
import com.spring.shiro.demo.filter.JwtFilter;
import com.spring.shiro.demo.realm.ShiroRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.SessionsSecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description: shiro配置
 * @Param:
 * @return:
 * @Author: wangliang
 * @Date: 2019/12/6
 */
@Slf4j
@Configuration
public class ShiroConfig {

    /**
     * shiro过滤器，可配置url,role,permiss信息
     *
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        // 自定义 OAuth2Filter 过滤器，替代默认的过滤器
        Map<String, Filter> filters = new HashMap<>();
//        filters.put("oauth2", new ShiroFilter());
        filters.put("jwt", new JwtFilter());
        filters.put("roles", rolesAuthorizationFilter());
        filters.put("perms", permissAuthorizationFilter());
        shiroFilter.setFilters(filters);
        // 访问路径拦截配置，"anon"表示无需验证，未登录也可访问
        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/webjars/**", "anon");
        // 查看SQL监控（druid）
        filterMap.put("/druid/**", "anon");
        // 首页和登录页面
        filterMap.put("/", "anon");
        filterMap.put("/sys/login", "anon");
        filterMap.put("/index/goIndexHtml", "anon");
        filterMap.put("/index.html", "anon");
        // swagger
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/swagger-resources", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/webjars/springfox-swagger-ui/**", "anon");
        // 其他所有路径交给OAuth2Filter处理
//        filterMap.put("/**", "oauth2");
        filterMap.put("/**", "jwt");
        filterMap.put("/sys/match", "roles[admin,user,1],perms[user:add]");
//        filterMap.put("/sys/match", "perms[user:add]");

        //未授权界面;
        shiroFilter.setUnauthorizedUrl("/403");
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }

    /**
     * 自定义的realm
     *
     * @return
     */
    @Bean
    public Realm getShiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
        shiroRealm.setCachingEnabled(true);
        shiroRealm.setAuthorizationCachingEnabled(true);//授权认证缓存
        shiroRealm.setAuthenticationCachingEnabled(true);//登录验证缓存
//        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRealm;
    }

    /**
     * securityManager
     *
     * @return
     */
    @Bean
    public SessionsSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 注入 Realm 实现类，实现自己的登录逻辑
        securityManager.setRealm(getShiroRealm());
        securityManager.setCacheManager(redisCacheManager());

        /*
         * 关闭shiro自带的session，详情见文档
         * http://shiro.apache.org/session-management.html#SessionManagement-StatelessApplications%28Sessionless%29
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisCacheManager redisCacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        redisCacheManager.setExpire(1800);//30分钟
        //指定存入Redis的主键
        redisCacheManager.setPrincipalIdFieldName("id");
        return redisCacheManager;
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        return redisManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        redisSessionDAO.setExpire(1800);
        return redisSessionDAO;
    }

    /**
     * 配置加密方式
     *
     * @return
     */
//    @Bean
//    public HashedCredentialsMatcher hashedCredentialsMatcher() {
//        //盐
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//        //配置散列算法，使用MD5加密算法
//        hashedCredentialsMatcher.setHashAlgorithmName("MD5");
//        //设置散列次数
//        hashedCredentialsMatcher.setHashIterations(2);
//        return hashedCredentialsMatcher;
//    }

    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * 自动创建代理，没有这个鉴权可能会出错
     *
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator autoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        autoProxyCreator.setProxyTargetClass(true);
        return autoProxyCreator;
    }

    /**
     * 自定义角色 filter
     *
     * @return
     */
    @Bean
    public CustomRolesAuthorizationFilter rolesAuthorizationFilter() {
        return new CustomRolesAuthorizationFilter();
    }

    /**
     * 自定义permiss filter
     *
     * @return
     */
    @Bean
    public CustomPermissionsAuthorizationFilter permissAuthorizationFilter() {
        return new CustomPermissionsAuthorizationFilter();
    }
}
