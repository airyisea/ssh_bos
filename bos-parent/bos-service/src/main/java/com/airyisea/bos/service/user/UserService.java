package com.airyisea.bos.service.user;

import java.util.List;

import com.airyisea.bos.domain.user.User;
import com.airyisea.bos.service.base.BaseService;

public interface UserService extends BaseService<User, Integer>{
	/*void save(User user);
	
	void delete(User user);
	
	User findOne(Integer id);
	
	List<User> findAll();*/
	
	void updateUser(User user);
	
	User login(String name,String password);

	void changePassword(String newPwd, Integer id);

	User findByUsername(String username);
}
