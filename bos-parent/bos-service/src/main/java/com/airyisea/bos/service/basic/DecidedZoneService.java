package com.airyisea.bos.service.basic;

import java.util.List;

import com.airyisea.bos.domain.basic.DecidedZone;
import com.airyisea.bos.service.base.BaseService;
import com.airyisea.crm.domain.customer.Customer;

public interface DecidedZoneService extends BaseService<DecidedZone, String>{

	/*Page<DecidedZone> queryPage(Specification<DecidedZone> condition,Pageable pageRequest);*/
	
	void add(DecidedZone model, String[] subareas);

	DecidedZone checkId(String id);

	List<Customer> getNoAssociationCustomer();

	List<Customer> getAssociationCustomer(String id);

	void assignCustomersToDecidedzone(String[] cids, String id);

}
