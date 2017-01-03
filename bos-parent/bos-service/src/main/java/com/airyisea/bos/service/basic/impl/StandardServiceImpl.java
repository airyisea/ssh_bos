package com.airyisea.bos.service.basic.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airyisea.bos.dao.basic.StandardDao;
import com.airyisea.bos.domain.basic.Standard;
import com.airyisea.bos.service.base.impl.BaseServiceImpl;
import com.airyisea.bos.service.basic.StandardService;
@Service("standardService")
@Transactional
public class StandardServiceImpl extends BaseServiceImpl<Standard, Integer> implements StandardService {
	@Autowired
	private StandardDao standardDao;
	@Autowired
	public void setSuperDao(StandardDao standardDao) {
		super.setDao(standardDao);
		this.standardDao = standardDao;
	}
	
	/*@Override
	public void add(Standard model) {
		standardDao.save(model);
	}
	@Override
	public Page<Standard> queryPage(Pageable pageable) {
		return standardDao.findAll(pageable);
	}
	@Override
	public List<Standard> findAll() {
		return standardDao.findAll();
	}*/
	
	@Override
	public Standard checkName(String name) {
		return standardDao.findByName(name);
	}
	@Override
	public void deleteBatch(String[] strings) {
		for (int i = 0; i < strings.length; i++) {
			standardDao.delete(Integer.parseInt(strings[i]));
		}
	}

}
