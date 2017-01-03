package com.airyisea.bos.service.basic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airyisea.bos.dao.basic.DecidedZoneDao;
import com.airyisea.bos.dao.basic.StaffDao;
import com.airyisea.bos.domain.basic.Staff;
import com.airyisea.bos.service.base.impl.BaseServiceImpl;
import com.airyisea.bos.service.basic.StaffService;
@Service("staffService")
@Transactional
public class StaffServiceImpl extends BaseServiceImpl<Staff, String> implements StaffService{
	private StaffDao staffDao;
	@Autowired
	public void setSuperDao(StaffDao staffDao) {
		super.setDao(staffDao);
		super.setSdao(staffDao);
		this.staffDao = staffDao;
	}
	@Autowired
	private DecidedZoneDao decidedZoneDao;
	
	/*@Override
	public void add(Staff model) {
		staffDao.save(model);
	}

	@Override
	public Page<Staff> queryPage(Pageable pageable) {
		return staffDao.findAll(pageable);
	}

	@Override
	public Page<Staff> queryPage(Specification<Staff> condition,
			Pageable pageable) {
		return staffDao.findAll(condition, pageable);
	}*/
	
	@Override
	public Staff checkPhone(String telephone) {
		return staffDao.findByTelephone(telephone);
	}

	@Override
	public void deleteBatch(String[] split) {
		for (int i = 0; i < split.length; i++) {
			Staff staff = new Staff();
			staff.setId(split[i]);
			decidedZoneDao.removeStaff(staff);
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
	public List<Staff> findInUseList() {
		return staffDao.findInUse();
	}

}
