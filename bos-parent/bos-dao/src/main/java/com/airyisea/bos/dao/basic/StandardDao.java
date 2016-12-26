package com.airyisea.bos.dao.basic;

import org.springframework.data.jpa.repository.JpaRepository;

import com.airyisea.bos.domain.basic.Standard;

public interface StandardDao extends JpaRepository<Standard, Integer> {

	Standard findByName(String name);

}
