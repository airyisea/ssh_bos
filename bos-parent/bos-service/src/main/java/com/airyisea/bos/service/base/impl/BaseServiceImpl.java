package com.airyisea.bos.service.base.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airyisea.bos.service.base.BaseService;
/*@Service
@Transactional*/
public class BaseServiceImpl<T,ID extends Serializable> implements BaseService<T, ID> {
	
	protected JpaRepository<T, ID> dao;
	protected JpaSpecificationExecutor<T> sdao;
	
	public void setDao(JpaRepository<T, ID> dao) {
		this.dao = dao;
	}

	public void setSdao(JpaSpecificationExecutor<T> sdao) {
		this.sdao = sdao;
	}

	@Override
	public void save(T t) {
		dao.save(t);
	}
	
	@Override
	public void save(Iterable<T> entities) {
		dao.save(entities);
	}

	@Override
	public void add(T t) {
		save(t);
	}

	@Override
	public void delete(T t) {
		dao.delete(t);
	}

	@Override
	public void delete(ID id) {
		dao.delete(id);
	}

	@Override
	public T findOne(ID id) {
		return dao.findOne(id);
	}
	
	@Override
	public T findById(ID id) {
		return findOne(id);
	}

	@Override
	public List<T> findAll() {
		return dao.findAll();
	}

	@Override
	public Page<T> queryPage(Pageable pageRequest) {
		return dao.findAll(pageRequest);
	}

	@Override
	public Page<T> queryPage(Specification<T> condition, Pageable pageRequest) {
		return sdao.findAll(condition, pageRequest);
	}


}
