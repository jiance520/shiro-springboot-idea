package com.service;

import com.entity.User;

import java.util.List;

public interface UserService {
	public User findByName(String name);
	public List<String> findPermissionByUserId(Integer userId);
}
