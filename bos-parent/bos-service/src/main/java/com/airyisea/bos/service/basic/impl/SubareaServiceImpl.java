package com.airyisea.bos.service.basic.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airyisea.bos.dao.basic.SubareaDao;
import com.airyisea.bos.domain.basic.DecidedZone;
import com.airyisea.bos.domain.basic.Subarea;
import com.airyisea.bos.service.base.impl.BaseServiceImpl;
import com.airyisea.bos.service.basic.SubareaService;

@Service("subareaService")
@Transactional
public class SubareaServiceImpl  extends BaseServiceImpl<Subarea, String> implements SubareaService {
	private SubareaDao subareaDao;
	@Autowired
	public void setSuperDao(SubareaDao subareaDao) {
		super.setDao(subareaDao);
		super.setSdao(subareaDao);
		this.subareaDao = subareaDao;
	}
	
	/*@Override
	public void add(Subarea model) {
		subareaDao.save(model);
	}
	
	@Override
	public void save(List<Subarea> list) {
		subareaDao.save(list);
	}

	@Override
	public Page<Subarea> queryPage(Pageable pageRequest) {
		return subareaDao.findAll(pageRequest);
	}
	
	@Override
	public Page<Subarea> queryPage(Specification<Subarea> condition,Pageable pageRequest) {
		return subareaDao.findAll(condition, pageRequest);
	}
*/
	@Override
	public Subarea checkId(String id) {
		return subareaDao.findOne(id);
	}

	@Override
	public List<Subarea> queryList(Specification<Subarea> condition) {
		return subareaDao.findAll(condition);
	}

	@Override
	public List<Subarea> findNoAssociation() {
		return subareaDao.findByDecidedZoneIsNull();
	}

	@Override
	public List<Subarea> findByDecidedZone(DecidedZone decidedZone) {
		return subareaDao.findByDecidedZone(decidedZone);
	}

	@Override
	public void removeDecidedZone(String[] ids) {
		for (String id : ids) {
			subareaDao.removeDecidedZone(id);
		}
		
	}



}
