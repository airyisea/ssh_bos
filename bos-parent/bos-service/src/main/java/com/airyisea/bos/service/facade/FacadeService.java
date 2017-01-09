package com.airyisea.bos.service.facade;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airyisea.bos.service.auth.FunctionService;
import com.airyisea.bos.service.auth.RoleService;
import com.airyisea.bos.service.basic.DecidedZoneService;
import com.airyisea.bos.service.basic.RegionService;
import com.airyisea.bos.service.basic.StaffService;
import com.airyisea.bos.service.basic.StandardService;
import com.airyisea.bos.service.basic.SubareaService;
import com.airyisea.bos.service.qp.NoticeBillService;
import com.airyisea.bos.service.qp.WorkBillService;
import com.airyisea.bos.service.qp.WorkOrderManageService;
import com.airyisea.bos.service.user.UserService;
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
	@Autowired
	private DecidedZoneService decidedZoneService;
	@Autowired
	private NoticeBillService noticeBillService;
	@Autowired
	private WorkBillService workBillService;
	@Autowired
	private WorkOrderManageService workOrderManageService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private FunctionService functionService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private HistoryService historyService;
	
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

	public DecidedZoneService getDecidedZoneService() {
		return decidedZoneService;
	}

	public NoticeBillService getNoticeBillService() {
		return noticeBillService;
	}

	public WorkBillService getWorkBillService() {
		return workBillService;
	}

	public WorkOrderManageService getWorkOrderManageService() {
		return workOrderManageService;
	}

	public RoleService getRoleService() {
		return roleService;
	}

	public FunctionService getFunctionService() {
		return functionService;
	}

	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

	public RuntimeService getRuntimeService() {
		return runtimeService;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public IdentityService getIdentityService() {
		return identityService;
	}

	public HistoryService getHistoryService() {
		return historyService;
	}
	
}
