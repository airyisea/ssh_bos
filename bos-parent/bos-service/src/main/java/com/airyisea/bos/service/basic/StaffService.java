package com.airyisea.bos.service.basic;

import java.util.List;

import com.airyisea.bos.domain.basic.Staff;
import com.airyisea.bos.service.base.BaseService;

public interface StaffService extends BaseService<Staff, String>{

	/*void add(Staff model);

	Page<Staff> queryPage(Pageable pageable);
	
	Page<Staff> queryPage(Specification<Staff> condition, Pageable pageable);*/

	Staff checkPhone(String telephone);

	void deleteBatch(String[] split);

	void restoreBatch(String[] split);

	List<Staff> findInUseList();


}
