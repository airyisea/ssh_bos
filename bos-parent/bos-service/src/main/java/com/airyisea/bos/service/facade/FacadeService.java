package com.airyisea.bos.service.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airyisea.bos.service.UserService;
import com.airyisea.bos.service.basic.RegionService;
import com.airyisea.bos.service.basic.StaffService;
import com.airyisea.bos.service.basic.StandardService;
import com.airyisea.bos.service.basic.SubareaService;
@Service("facadeService")
public class FacadeService {
	@Autowired
	private UserService userService;
	@Autowired
	private StandardService standardService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private RegionService regionService;
	@Autowired
	private SubareaService subareaService;
	
	public StandardService getStandardService() {
		return standardService;
	}

	public UserService getUserService() {
		return userService;
	}

	public StaffService getStaffService() {
		return staffService;
	}

	public RegionService getRegionService() {
		return regionService;
	}

	public SubareaService getSubareaService() {
		return subareaService;
	}
	
	
	
}
