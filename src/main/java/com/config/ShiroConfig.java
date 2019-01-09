package com.config;
import java.util.*;
import javax.servlet.Filter;

import com.filter.UserFormAuthenticationFilter;
import com.realm.MyRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.*;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
//shiro配置类，由于不能在xml里配置，只能在类里面配置
@Configuration
public class ShiroConfig {
    //     ShrioFilterFactory
    @Bean
    public ShiroFilterFactoryBean  shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
//           关联SecurityManager
        bean.setSecurityManager(securityManager);
        Map<String,String> filterMap = new LinkedHashMap();
//           认证过滤器,不是在shiro.ini里配置
//           放行登陆页面
        filterMap.put("/user/login", "anon");
//           放行验证码请求
        filterMap.put("/defaultKaptcha", "anon");
//           授权过滤器
        filterMap.put("/product/toAdd", "perms[product:add]");
        filterMap.put("/product/toList", "perms[product:list]");
        filterMap.put("/product/toUpdate", "perms[product:update]");
//           添加user过滤器，只要控制层判断index使用了，rememberMe则放行。与UserController中的token.setRememberMe(true)是一对开关组合。
//           filterMap.put("/index","user");
        filterMap.put("/index","userFilter");//重点，前面的/不能省！自定义userFilter过滤器解决了session丢失问题。
        filterMap.put("/**", "authc");//一定要放在filterMap.put("/product/toUpdate", "perms[product:update]")之后！重点。否则授权过滤无效！
//           把自定义Filter添加到shiroFilter里面
        Map<String,Filter> filters = new LinkedHashMap();
        filters.put("userFilter", userFormAuthenticationFilter());
        bean.setFilters(filters);
//           添加shiro过滤器
        bean.setFilterChainDefinitionMap(filterMap);
//           修改登陆请求
        bean.setLoginUrl("/toLogin");
//           未授权提示页面
        bean.setUnauthorizedUrl("/unAuth");
        return bean;
    }
    //     创建自定义过滤器，判断记住我时session是否失效。
    @Bean
    public UserFormAuthenticationFilter userFormAuthenticationFilter() {
        return new UserFormAuthenticationFilter();
    }
    //     创建SecurityManager
    @Bean(name="userFilter")
    public DefaultWebSecurityManager defaultWebSecurityManager(MyRealm myrealm, CookieRememberMeManager rememberMeManager) {
        DefaultWebSecurityManager manager = new  DefaultWebSecurityManager();
//           关联MyRealm
        manager.setRealm(myrealm);
//           关联rememberMeManager
        manager.setRememberMeManager(rememberMeManager);
        return manager;
    }
    //     创建cookie manager
    @Bean
    public CookieRememberMeManager cookieRememberMeManager(SimpleCookie  cookie) {
        CookieRememberMeManager manager = new CookieRememberMeManager();
        manager.setCookie(cookie);
        return manager;
    }
    //     RememberMe,cookie
    @Bean
    public SimpleCookie SimpleCookie() {
        SimpleCookie cookie = new SimpleCookie("rememberMe");
//           设置cookie 时间长。
        cookie.setMaxAge(120);
//           设置cookie为只读模式，提高安全性
        cookie.setHttpOnly(true);
        return cookie;
    }
    //     创建MyRealm
    @Bean
    public MyRealm myrealm() {
        MyRealm myrealm= new MyRealm();
        return myrealm;
    }
    //     整合shiro
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}