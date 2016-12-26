package com.airyisea.bos.service.basic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.airyisea.bos.dao.basic.StandardDao;
import com.airyisea.bos.domain.basic.Standard;
import com.airyisea.bos.service.basic.StandardService;
@Service("standardService")
public class StandardServiceImpl implements StandardService {
	@Autowired
	private StandardDao standardDao;
	@Override
	public void add(Standard model) {
		standardDao.save(model);
	}
	@Override
	public Standard checkName(String name) {
		return standardDao.findByName(name);
	}
	@Override
	public Page<Standard> queryPage(Pageable pageable) {
		return standardDao.findAll(pageable);
	}
	@Override
	public void deleteBatch(String[] strings) {
		for (int i = 0; i < strings.length; i++) {
			standardDao.delete(Integer.parseInt(strings[i]));
		}
		
	}
	@Override
	public List<Standard> queryAll() {
		return standardDao.findAll();
	}

}
