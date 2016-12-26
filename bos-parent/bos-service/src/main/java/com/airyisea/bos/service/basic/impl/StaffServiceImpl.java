package com.airyisea.bos.service.basic.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airyisea.bos.dao.basic.StaffDao;
import com.airyisea.bos.domain.basic.Staff;
import com.airyisea.bos.service.basic.StaffService;
@Service("staffService")
@Transactional
public class StaffServiceImpl implements StaffService{
	@Autowired
	private StaffDao staffDao;
	
	@Override
	public void add(Staff model) {
		staffDao.save(model);
	}

	@Override
	public Staff checkPhone(String telephone) {
		return staffDao.findByTelephone(telephone);
	}

	@Override
	public Page<Staff> queryPage(Pageable pageable) {
		return staffDao.findAll(pageable);
	}

	@Override
	public void deleteBatch(String[] split) {
		for (int i = 0; i < split.length; i++) {
			staffDao.discard(split[i]);
		}
		
	}

	@Override
	public void restoreBatch(String[] split) {
		for (int i = 0; i < split.length; i++) {
			staffDao.restore(split[i]);
		}
	}

	@Override
	public Page<Staff> queryPage(Specification<Staff> condition,
			Pageable pageable) {
		return staffDao.findAll(condition, pageable);
	}

}
