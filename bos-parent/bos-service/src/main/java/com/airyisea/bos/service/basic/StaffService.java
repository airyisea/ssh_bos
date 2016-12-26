package com.airyisea.bos.service.basic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.airyisea.bos.domain.basic.Staff;

public interface StaffService {

	void add(Staff model);

	Staff checkPhone(String telephone);

	Page<Staff> queryPage(Pageable pageable);
	
	Page<Staff> queryPage(Specification<Staff> condition, Pageable pageable);

	void deleteBatch(String[] split);

	void restoreBatch(String[] split);


}
