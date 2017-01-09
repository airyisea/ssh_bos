package com.airyisea.bos.service.auth.impl;

import java.util.List;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airyisea.bos.dao.auth.FunctionDao;
import com.airyisea.bos.dao.auth.RoleDao;
import com.airyisea.bos.domain.auth.Function;
import com.airyisea.bos.domain.auth.Role;
import com.airyisea.bos.service.auth.RoleService;
import com.airyisea.bos.service.base.impl.BaseServiceImpl;
@Service("roleService")
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<Role, String> implements RoleService {
	@Autowired
	private IdentityService identityService;
	
	private RoleDao roleDao;
	@Autowired
	public void setSuperDao(RoleDao roleDao) {
		super.setDao(roleDao);
		super.setSdao(roleDao);
		this.roleDao = roleDao;
	}
	@Autowired
	private FunctionDao functionDao;
	
	@Override
	public List<Role> findByUserId(Integer uid) {
		return roleDao.findByUserId(uid);
	}

	@Override
	public Role findByCode(String code) {
		return roleDao.findByCode(code);
	}

	@Override
	public void add(Role model, String fids) {
		roleDao.save(model);
		Group group = new GroupEntity();
		group.setId(model.getCode());
		group.setName(model.getName());
		identityService.saveGroup(group);
		if(StringUtils.isNotBlank(fids)) {
			String[] fidsArr = fids.split(",");
			for (String fid : fidsArr) {
				//Function function = functionDao.findOne(fid);
				Function function = new Function();
				function.setId(fid);
				model.getFunctions().add(function);
			}
		}
		
	}

}
