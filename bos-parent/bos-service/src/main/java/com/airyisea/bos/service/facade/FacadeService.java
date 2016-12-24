package com.airyisea.bos.service.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airyisea.bos.service.UserService;
import com.airyisea.bos.service.basic.StandardService;
@Service("facadeService")
public class FacadeService {
	@Autowired
	private UserService userService;
	@Autowired
	private StandardService standardService;
	
	public StandardService getStandardService() {
		return standardService;
	}

	public UserService getUserService() {
		return userService;
	}
	
	
	
}
