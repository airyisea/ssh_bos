package com.airyisea.bos.service.auth.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airyisea.bos.dao.auth.FunctionDao;
import com.airyisea.bos.domain.auth.Function;
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

}
