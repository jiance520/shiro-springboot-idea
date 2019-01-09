package com.realm;

import java.util.List;
import javax.annotation.Resource;

import com.entity.User;
import com.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.*;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.*;
import org.springframework.util.StringUtils;
public class MyRealm extends AuthorizingRealm{
    @Resource
    public UserService userService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//        info.addStringPermission("product:add");
//        得到当前用户
        Subject subject = SecurityUtils.getSubject();
        User dbUser = (User)subject.getPrincipal();
        List<String> perms = userService.findPermissionByUserId(dbUser.getId());
        if(perms!=null) {
            for(String perm:perms) {
                if(!StringUtils.isEmpty(perm)) {
                    info.addStringPermission(perm);
                }
            }
        }
        return info;
    }
    // 认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
        System.out.println("执行认证...");
        UsernamePasswordToken token = (UsernamePasswordToken)arg0;
        //模拟数据库数据
        /*String name = "jack";
        String password = "1234";
        if(!name.equals(token.getUsername())) {
            return null;//login会抛出UnknownAccountException异常。
        }
        User dbUser = new User();
        dbUser.setName(name);
        dbUser.setPassword(password);*/
        User dbUser = userService.findByName(token.getUsername());
        if(dbUser==null) {
            return null;//用户不存在
        }
        return new SimpleAuthenticationInfo(dbUser, dbUser.getPassword(), "");//第一、二个参数可以自己指定返回值。
    }
}