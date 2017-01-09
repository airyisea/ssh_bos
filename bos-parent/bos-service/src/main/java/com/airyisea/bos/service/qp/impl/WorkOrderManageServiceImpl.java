package com.airyisea.bos.service.qp.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airyisea.bos.dao.qp.WorkOrderManageDao;
import com.airyisea.bos.domain.qp.WorkOrderManage;
import com.airyisea.bos.service.base.impl.BaseServiceImpl;
import com.airyisea.bos.service.facade.FacadeService;
import com.airyisea.bos.service.qp.WorkOrderManageService;
@Service("workOrderManageService")
@Transactional
public class WorkOrderManageServiceImpl extends BaseServiceImpl<WorkOrderManage, String> implements WorkOrderManageService{
	@Autowired
	private FacadeService facadeService;
	
	
	private WorkOrderManageDao workOrderManageDao;
	@Autowired
	public void setSuperDao(WorkOrderManageDao workOrderManageDao){
		super.setDao(workOrderManageDao);
		super.setSdao(workOrderManageDao);
		this.workOrderManageDao = workOrderManageDao;
	}
	@Override
	public Page<WorkOrderManage> queryPage(Pageable pageRequest,
			String conditionName, String conditionValue) {
		return workOrderManageDao.queryIndex(pageRequest,conditionName,conditionValue);
	}
	@Override
	public List<WorkOrderManage> findNoStart() {
		return workOrderManageDao.findByStart("0");
	}
	@Override
	public void start(String id) {
		WorkOrderManage workOrderManage = workOrderManageDao.findOne(id);
		workOrderManage.setStart("1");
		
		String processDefinitionKey = "transfer";
		String businessKey = id;
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("业务数据", workOrderManage);
		//启动流程实例
		facadeService.getRuntimeService().startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
	}
	@Override
	public void check(String taskId, String check) {
		facadeService.getTaskService().setVariable(taskId, "check", check);
		String processInstanceId = facadeService.getTaskService().createTaskQuery().taskId(taskId).singleResult().getProcessInstanceId();
		ProcessInstance processInstance = facadeService.getRuntimeService().createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		String businessKey = processInstance.getBusinessKey();
		WorkOrderManage workOrderManage = workOrderManageDao.findOne(businessKey);
		facadeService.getTaskService().complete(taskId);
		if("1".equals(check)) {
			workOrderManage.setManagercheck("1");
		}else {
			workOrderManage.setStart("0");
			facadeService.getHistoryService().deleteHistoricProcessInstance(processInstanceId);
		}
		
	}
	
}
