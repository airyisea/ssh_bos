package com.airyisea.bos.service.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airyisea.bos.service.UserService;
@Service("facadeService")
public class FacadeService {
	@Autowired
	private UserService userService;

	public UserService getUserService() {
		return userService;
	}
	
	
}
