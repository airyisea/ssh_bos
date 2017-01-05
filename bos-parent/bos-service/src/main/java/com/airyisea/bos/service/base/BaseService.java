package com.airyisea.bos.service.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface BaseService<T,ID extends Serializable> {
	
	void save(T t);
	
	void save(Iterable<T> entities);
	
	void add(T t);
	
	void delete(T t);
	
	void delete(ID id);
	
	T findOne(ID id);
	
	T findById(ID id);
	
	List<T> findAll();
	
	Page<T> queryPage(Pageable pageRequest);
	
	Page<T> queryPage(Specification<T> condition,Pageable pageRequest);
	
	Page<T> queryPage(Specification<T> condition,Pageable pageRequest,Object param);
	
	
}
