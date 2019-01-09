package com.action;

import javax.servlet.http.HttpServletRequest;

import com.entity.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.alibaba.druid.util.StringUtils;
@Controller
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/login")
    public String login(User user, String rememberMe, String  code, HttpServletRequest request, Model model) {
//           判断验证码是否正确
        if(!StringUtils.isEmpty(code)) {
//                  取出生成的验证码
            String vrifyCode =  (String)request.getSession().getAttribute("vrifyCode");
            if(!vrifyCode.equals(code)) {
                model.addAttribute("msg", "验证码输入有误");
                return "login";
            }
        }
//           使用shiro进行登陆
        Subject subject = SecurityUtils.getSubject();
//           使用MD5加密用户密码后与数据库加密的密码核对
        Md5Hash hash = new Md5Hash(user.getPassword(), user.getName(), 2);
//           UsernamePasswordToken token = new  UsernamePasswordToken(user.getName(),user.getPassword());
        UsernamePasswordToken token = new  UsernamePasswordToken(user.getName(),hash.toString());
        if(rememberMe!=null && rememberMe.equals("1")) {//对于checkbox，如果没有选中，默认值是null! 后面的rememberMe.equals("1")应该可以去掉？
            token.setRememberMe(true);//与filterMap.put("/index","user");构成组合。判断页面是否使用了记住我。
        }
        try {
            subject.login(token);
//                  登陆成功
            User dbuser = (User)subject.getPrincipal();
            request.getSession().setAttribute("username",  dbuser.getName());
            return "redirect:/index";

        } catch (UnknownAccountException e) {
            model.addAttribute("msg", "用户名不存在");
            return "login";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("msg", "密码错误");
            return "login";
        }
    }
    @RequestMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();//删除shiro的session会员信息
        return "redirect:/toLogin";
    }
}