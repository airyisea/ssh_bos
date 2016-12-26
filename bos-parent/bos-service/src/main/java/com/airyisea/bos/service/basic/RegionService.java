package com.airyisea.bos.service.basic;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.airyisea.bos.domain.basic.Region;

public interface RegionService {

	void save(List<Region> list);

	Page<Region> queryPage(Pageable pageRequest);

	void add(Region model);

	Region checkId(String id);

	Page<Region> queryPage(Specification<Region> andCondition,
			Pageable pageRequest);

}
