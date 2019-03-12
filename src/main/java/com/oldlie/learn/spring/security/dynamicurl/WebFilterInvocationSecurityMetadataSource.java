package com.oldlie.learn.spring.security.dynamicurl;

import com.oldlie.learn.spring.security.dynamicurl.service.UserDetailsImplService;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.ExpressionBasedFilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;

import java.util.*;

/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃  神兽保佑
 * 　　　　┃　　　┃  代码无bug
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━━━━━━━━━━━━
 * 2019/03/12
 * WebFilterInvocationSecurityMetadataSource
 *
 * @author 陈列
 */
public class WebFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    private FilterInvocationSecurityMetadataSource superMetadataSource;
    private UserDetailsImplService userDetailsImplService;

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    private Map<String, String> urlRoleMap = new HashMap<String, String>();

    public WebFilterInvocationSecurityMetadataSource(
            FilterInvocationSecurityMetadataSource expressionBasedFilterInvocationSecurityMetadataSource,
            UserDetailsImplService userDetailsImplService) {
        this.superMetadataSource = expressionBasedFilterInvocationSecurityMetadataSource;
        this.userDetailsImplService = userDetailsImplService;
        // TODO 从数据库加载权限配置
        // this.urlRoleMap = this.userDetailsImplService.loadUrlMap();
    }

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    // 这里的需要从DB加载
//    private final Map<String, String> urlRoleMap = new HashMap<String, String>() {{
//        put("/open/**", "ROLE_ANONYMOUS");
//        put("/health", "ROLE_ANONYMOUS");
//        put("/restart", "ROLE_ADMIN");
//        put("/demo", "ROLE_USER");
//    }};

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        this.urlRoleMap = this.userDetailsImplService.loadUrlMap();

        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();

        for (Map.Entry<String, String> entry : urlRoleMap.entrySet()) {
            if (antPathMatcher.match(entry.getKey(), url)) {

                SecurityConfig.createList();


                String[] roles = entry.getValue().split(",");
                List<ConfigAttribute> attributes = new ArrayList<>(roles.length);

                for (String attribute : roles) {
                    attributes.add(new SecurityConfig(attribute.trim()));
                }

                return attributes;
            }
        }

        //  返回代码定义的默认配置
        return superMetadataSource.getAttributes(object);
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
