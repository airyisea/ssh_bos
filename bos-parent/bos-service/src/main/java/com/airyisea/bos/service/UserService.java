package com.airyisea.bos.service;

import java.util.List;

import com.airyisea.bos.domain.user.User;

public interface UserService {
	void save(User user);
	
	void delete(User user);
	
	User findById(Integer id);
	
	List<User> findAll();
	
	void updateUser(User user);
	
	User login(String name,String password);

	void changePassword(String newPwd, Integer id);
}
