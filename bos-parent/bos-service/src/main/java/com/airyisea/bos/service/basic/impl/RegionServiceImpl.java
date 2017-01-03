package com.airyisea.bos.service.basic.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airyisea.bos.dao.basic.RegionDao;
import com.airyisea.bos.domain.basic.Region;
import com.airyisea.bos.service.base.impl.BaseServiceImpl;
import com.airyisea.bos.service.basic.RegionService;
@Service("regionService")
@Transactional
public class RegionServiceImpl  extends BaseServiceImpl<Region, String> implements RegionService {
	
	private RegionDao regionDao;
	
	@Autowired
	public void setSuperDao(RegionDao regionDao) {
		super.setDao(regionDao);
		super.setSdao(regionDao);
		this.regionDao = regionDao;
	}
	
	/*@Override
	public void save(List<Region> list) {
		regionDao.save(list);
	}

	@Override
	public Page<Region> queryPage(Pageable pageRequest) {
		return regionDao.findAll(pageRequest);
	}

	@Override
	public void add(Region model) {
		regionDao.save(model);
	}
	
	@Override
	public Page<Region> queryPage(Specification<Region> condition,
			Pageable pageRequest) {
		return regionDao.findAll(condition, pageRequest);
	}*/

	@Override
	public Region checkId(String id) {
		return regionDao.findOne(id);
	}

	@Override
	public List<Region> queryList() {
		return regionDao.findAll();
	}

	@Override
	public List<Region> queryList(String param) {
		if(StringUtils.isNotBlank(param)) {
			return regionDao.findByProvinceOrCityOrDistrict("%" + param + "%");
		}else {
			return queryList();
		}
	}

	@Override
	public List<String> getPCD(String province, String city) {
		if(province != null) {
			if(city != null) {
				//查询区
				return regionDao.findDistrictByProvinceAndCity(province,city);
			}else {
				//查询市
				return regionDao.findCityByProvince(province);
			}
		}else {
			//查询省
			return regionDao.findProvince();
		}
	}

}
