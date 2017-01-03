package com.airyisea.bos.service.basic;

import java.util.List;

import com.airyisea.bos.domain.basic.Region;
import com.airyisea.bos.service.base.BaseService;

public interface RegionService extends BaseService<Region, String>{

	/*void save(List<Region> list);

	Page<Region> queryPage(Pageable pageRequest);

	void add(Region model);

	Page<Region> queryPage(Specification<Region> andCondition,
			Pageable pageRequest);*/

	Region checkId(String id);

	List<Region> queryList();

	List<Region> queryList(String parameter);

	List<String> getPCD(String province, String city);

}
