package com.service.impl;

import java.util.List;
import javax.annotation.Resource;

import com.dao.UserMapper;
import com.entity.User;
import com.service.UserService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Transactional
@MapperScan(basePackages = "com.dao")
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
	@Resource
	private UserMapper userMapper;
	@Override
	public User findByName(String name) {

		return userMapper.findByName(name);
	}
	@Override
	public List<String> findPermissionByUserId(Integer userId) {
		return userMapper.findPermissionByUserId(userId);
	}
}
