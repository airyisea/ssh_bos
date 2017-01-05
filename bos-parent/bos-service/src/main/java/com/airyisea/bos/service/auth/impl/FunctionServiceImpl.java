package com.airyisea.bos.service.auth.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airyisea.bos.dao.auth.FunctionDao;
import com.airyisea.bos.domain.auth.Function;
import com.airyisea.bos.domain.user.User;
import com.airyisea.bos.service.auth.FunctionService;
import com.airyisea.bos.service.base.impl.BaseServiceImpl;
@Service("functionService")
@Transactional
public class FunctionServiceImpl extends BaseServiceImpl<Function, String> implements FunctionService {
	private FunctionDao functionDao;
	@Autowired
	public void setSuperDao(FunctionDao functionDao) {
		super.setDao(functionDao);
		super.setSdao(functionDao);
		this.functionDao = functionDao;
	}
	@Override
	public List<Function> findParentList() {
		return functionDao.findParentList();
	}
	@Cacheable(key="#loginUser.id",value="function")
	@Override
	public List<Function> findByUser(User loginUser) {
		if("admin".equals(loginUser.getUsername())) {
			return functionDao.findAll();
		}
		return functionDao.findByUserId(loginUser.getId());
	}

}
