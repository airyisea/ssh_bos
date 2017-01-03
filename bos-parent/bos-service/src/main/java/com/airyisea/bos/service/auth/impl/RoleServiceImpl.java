package com.airyisea.bos.service.auth.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airyisea.bos.dao.auth.FunctionDao;
import com.airyisea.bos.dao.auth.RoleDao;
import com.airyisea.bos.domain.auth.Role;
import com.airyisea.bos.service.auth.RoleService;
import com.airyisea.bos.service.base.impl.BaseServiceImpl;
@Service("roleService")
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<Role, String> implements RoleService {
	private RoleDao roleDao;
	@Autowired
	public void setSuperDao(RoleDao roleDao) {
		super.setDao(roleDao);
		super.setSdao(roleDao);
		this.roleDao = roleDao;
	}
	
	@Override
	public List<Role> findByUserId(Integer uid) {
		return roleDao.findByUserId(uid);
	}

}
