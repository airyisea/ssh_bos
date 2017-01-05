package com.airyisea.bos.service.qp;

import java.util.List;

import com.airyisea.bos.domain.qp.NoticeBill;
import com.airyisea.bos.service.base.BaseService;
import com.airyisea.crm.domain.customer.Customer;

public interface NoticeBillService extends BaseService<NoticeBill, String>{

	Customer checkTel(String telephone);

	void add(NoticeBill model, String province, String city, String district);

	List<NoticeBill> findNoAssociations();

	void createManualWorkBill(NoticeBill model);

}
