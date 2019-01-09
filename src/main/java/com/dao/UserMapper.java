package com.dao;
import java.util.List;

import com.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper{
//	根据用户名查询用户
	public User findByName(String name);
//	根据用户ID，查询用户拥有的资源授权码
	public List<String> findPermissionByUserId(Integer userId);
//	更新用户密码
	public void updatePassword(User user);
}
