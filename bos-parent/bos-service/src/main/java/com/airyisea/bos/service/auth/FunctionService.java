package com.airyisea.bos.service.auth;

import java.util.List;

import com.airyisea.bos.domain.auth.Function;
import com.airyisea.bos.domain.user.User;
import com.airyisea.bos.service.base.BaseService;

public interface FunctionService extends BaseService<Function, String>{

	List<Function> findParentList();

	List<Function> findByUser(User loginUser);
	
	
}
