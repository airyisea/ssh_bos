package com.airyisea.bos.service.user.impl;

import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airyisea.bos.dao.auth.RoleDao;
import com.airyisea.bos.dao.user.UserDao;
import com.airyisea.bos.domain.auth.Role;
import com.airyisea.bos.domain.user.User;
import com.airyisea.bos.service.base.impl.BaseServiceImpl;
import com.airyisea.bos.service.user.UserService;
import com.airyisea.bos.utils.MD5Utils;
@Service("userService")
@Transactional
public class UserServiceImpl extends BaseServiceImpl<User, Integer> implements UserService{
	@Autowired
	private IdentityService identityService;
	@Autowired
	private RoleDao roleDao;
	
	private UserDao userDao;
	@Autowired
	public void setSuperDao(UserDao userDao){
		super.setDao(userDao);
		super.setSdao(userDao);
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

	@Override
	public User findByPhone(String telephone) {
		return userDao.findByTelephone(telephone);
	}

	@Override
	public void add(User model, String[] rids) {
		model.setPassword(MD5Utils.getPwd(model.getPassword()));
		userDao.saveAndFlush(model);
		org.activiti.engine.identity.User user = new org.activiti.engine.impl.persistence.entity.UserEntity();
		user.setId(model.getId() + "");
		identityService.saveUser(user);
		if(rids!= null && rids.length != 0) {
			for (String rid : rids) {
				Role role = roleDao.findOne(rid);
				model.getRoles().add(role);
				identityService.createMembership(model.getId() + "", role.getCode());
			}
		}
		
	}

	

}
