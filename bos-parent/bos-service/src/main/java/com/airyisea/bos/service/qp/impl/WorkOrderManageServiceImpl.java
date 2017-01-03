package com.airyisea.bos.service.qp.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airyisea.bos.dao.qp.WorkOrderManageDao;
import com.airyisea.bos.domain.qp.WorkOrderManage;
import com.airyisea.bos.service.base.impl.BaseServiceImpl;
import com.airyisea.bos.service.qp.WorkOrderManageService;
@Service("workOrderManageService")
@Transactional
public class WorkOrderManageServiceImpl extends BaseServiceImpl<WorkOrderManage, String> implements WorkOrderManageService{
	
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
	
}
