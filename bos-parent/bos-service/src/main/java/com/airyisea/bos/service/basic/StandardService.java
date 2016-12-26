package com.airyisea.bos.service.basic;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.airyisea.bos.domain.basic.Standard;

public interface StandardService {
	void add(Standard model);

	Standard checkName(String name);

	Page<Standard> queryPage(Pageable pageable);

	void deleteBatch(String[] strings);

	List<Standard> queryAll();
}
