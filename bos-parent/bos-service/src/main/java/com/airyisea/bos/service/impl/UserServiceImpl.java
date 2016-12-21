package com.airyisea.bos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airyisea.bos.dao.user.UserDao;
import com.airyisea.bos.domain.user.User;
import com.airyisea.bos.service.UserService;
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{
	@Autowired
	private UserDao userDao;
	
	@Override
	public void save(User user) {
		userDao.save(user);
		
	}

	@Override
	public void delete(User user) {
		userDao.delete(user);
		
	}

	@Override
	public User findById(Integer id) {
		return userDao.findOne(id);
	}

	@Override
	public List<User> findAll() {
		return userDao.findAll();
	}

	@Override
	public void updateUser(User user) {
		userDao.save(user);
	}
	
	public User login(String name,String password){
		return userDao.findByUsernameAndPassword(name, password);
	}
	

}
