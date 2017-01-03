package com.airyisea.bos.service.qp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.airyisea.bos.domain.qp.WorkBill;
import com.airyisea.bos.service.base.BaseService;

public interface WorkBillService extends BaseService<WorkBill, String>{

	/*Page<WorkBill> queryPage(Specification<WorkBill> condition, Pageable pageRequest);*/

	void repeat(String param);

	void cancel(String param);

}
