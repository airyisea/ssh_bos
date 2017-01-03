package com.airyisea.bos.service.qp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.airyisea.bos.domain.qp.WorkOrderManage;
import com.airyisea.bos.service.base.BaseService;

public interface WorkOrderManageService extends BaseService<WorkOrderManage, String> {

	Page<WorkOrderManage> queryPage(Pageable pageRequest, String conditionName,
			String conditionValue);

}
