package com.airyisea.bos.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airyisea.bos.dao.user.UserDao;
import com.airyisea.bos.domain.user.User;
import com.airyisea.bos.service.base.impl.BaseServiceImpl;
import com.airyisea.bos.service.user.UserService;
import com.airyisea.bos.utils.MD5Utils;
@Service("userService")
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User, Integer> implements UserService{
	private UserDao userDao;
	
	@Autowired
	public void setSuperDao(UserDao userDao){
		super.setDao(userDao);
		this.userDao = userDao;
	}
	
	/*@Override
	public void save(User user) {
		user.setPassword(MD5Utils.getPwd(user.getPassword()));
		userDao.save(user);
		
	}

	@Override
	public void delete(User user) {
		userDao.delete(user);
		
	}

	@Override
	public User findOne(Integer id) {
		return userDao.findOne(id);
	}

	@Override
	public List<User> findAll() {
		return userDao.findAll();
	}*/

	@Override
	public void updateUser(User user) {
		userDao.save(user);
	}
	@Override
	public User login(String name,String password){
		return userDao.findByUsernameAndPassword(name, MD5Utils.getPwd(password));
	}

	@Override
	public void changePassword(String newPwd, Integer id) {
		userDao.updatePassword(MD5Utils.getPwd(newPwd),id);
		
	}

	@Override
	public User findByUsername(String username) {
		return userDao.findByUsername(username);
	}
	

}