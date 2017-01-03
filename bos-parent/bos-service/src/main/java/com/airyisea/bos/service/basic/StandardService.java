package com.airyisea.bos.service.basic;

import com.airyisea.bos.domain.basic.Standard;
import com.airyisea.bos.service.base.BaseService;

public interface StandardService extends BaseService<Standard, Integer>{
	/*void add(Standard model);

	List<Standard> findAll();

	Page<Standard> queryPage(Pageable pageable);*/

	void deleteBatch(String[] strings);

	Standard checkName(String name);
}
