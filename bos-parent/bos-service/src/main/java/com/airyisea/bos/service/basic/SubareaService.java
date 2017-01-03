package com.airyisea.bos.service.basic;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.airyisea.bos.domain.basic.DecidedZone;
import com.airyisea.bos.domain.basic.Subarea;
import com.airyisea.bos.service.base.BaseService;

public interface SubareaService extends BaseService<Subarea, String>{

	/*void add(Subarea model);
	
	void add(List<Subarea> list);

	Page<Subarea> queryPage(Pageable pageRequest);
	
	Page<Subarea> queryPage(Specification<Subarea> condition, Pageable pageRequest);*/

	Subarea checkId(String id);

	List<Subarea> queryList(Specification<Subarea> condition);

	List<Subarea> findNoAssociation();

	List<Subarea> findByDecidedZone(DecidedZone decidedZone);

	void removeDecidedZone(String[] ids);



}
