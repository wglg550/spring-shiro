package com.spring.shiro.demo.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 基于URL的权限判断过滤器<p>
 * 我们自动根据URL产生所谓的权限字符串，这一项在Shiro示例中是写在配置文件里面的，默认认为权限不可动态配置<p>
 * URL举例：/User/create.do?***=***  -->权限字符串：/User/create.do
 *
 * @author zhengwei lastmodified 2013年8月15日
 */
public class CustomPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {
    /**
     * @param mappedValue 指的是在声明url时指定的权限字符串，如/User/create.do=perms[User:create].我们要动态产生这个权限字符串，所以这个配置对我们没用
     */
    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {
        Subject subject = getSubject(request, response);
        String[] permissArray = (String[]) mappedValue;
        // 如果没有权限限制，直接放行
        if (permissArray == null || permissArray.length == 0) {
            return true;
        }
        // 判断是否有角色权限
        for (int idx = 0; idx < permissArray.length; idx++) {
            if (subject.isPermitted(permissArray[idx])) {
                return true;
            }
        }
        return false;
    }
}