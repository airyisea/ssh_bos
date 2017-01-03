package com.airyisea.bos.service.qp.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airyisea.bos.dao.basic.RegionDao;
import com.airyisea.bos.dao.qp.WorkBillDao;
import com.airyisea.bos.domain.qp.WorkBill;
import com.airyisea.bos.service.base.impl.BaseServiceImpl;
import com.airyisea.bos.service.qp.WorkBillService;

@Service("workBillService")
@Transactional
public class WorkBillServiceImpl extends BaseServiceImpl<WorkBill, String> implements WorkBillService {
	private WorkBillDao workBillDao;
	@Autowired
	public void setSuperDao(WorkBillDao workBillDao) {
		super.setDao(workBillDao);
		super.setSdao(workBillDao);
		this.workBillDao = workBillDao;
	}
	/*@Override
	public Page<WorkBill> queryPage(Specification<WorkBill> condition, Pageable pageRequest) {
		return workBillDao.findAll(condition, pageRequest);
	}*/

	@Override
	public void repeat(String param) {
		String[] ids = param.split(",");
		for (String id : ids) {
			workBillDao.addAttachbilltimes(id);
		}
	}

	@Override
	public void cancel(String param) {
		String[] ids = param.split(",");
		for (String id : ids) {
			workBillDao.cancel(id);
		}
		
	}

}
