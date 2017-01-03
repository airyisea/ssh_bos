package com.airyisea.bos.dao.qp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.airyisea.bos.domain.qp.WorkOrderManage;

public interface WorkOrderManageDao extends JpaRepository<WorkOrderManage, String>,JpaSpecificationExecutor<WorkOrderManage>{
	
	Page<WorkOrderManage> queryIndex(Pageable pageRequest, String conditionName, String conditionValue);
}
