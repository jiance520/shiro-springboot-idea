package com.test;

import com.App;
import com.dao.UserMapper;
import com.entity.User;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

//SpringJUnit支持，由此引入Spring-Test框架支持！
@RunWith(SpringJUnit4ClassRunner.class)
//加载启动器类
@SpringBootTest(classes= App.class)
//由于是Web项目，Junit需要模拟ServletContext，
// 因此我们需要给我们的测试类加上@WebAppConfiguration。
@WebAppConfiguration
public class UserMapperTest {
    @Resource
    public UserMapper userMapper;
    @Test
    @Transactional
    @Rollback
    public void testFindByName() {
        User user = userMapper.findByName("eric");
        System.out.println(user);
    }
    @Test
    @Transactional
    @Rollback
    public void testUpdatePassword() {
        User user = new User();
        user.setId(1);
//		传统MD5在mysql中的值：select MD5("1234");
//		使用shiro的加密工具进行加密,与传统的MD5加密值有区别，source要加密的内容，salt盐，就是加密的修饰(可以根用户名变，也可以是定值)，hashIterations加密次数。
//		Md5Hash md5hash = new Md5Hash(source, salt, hashIterations);
        Md5Hash hash = new Md5Hash("1234", "eric", 2);
        user.setPassword(hash.toString());
        userMapper.updatePassword(user);
        System.out.println(user);
    }
}