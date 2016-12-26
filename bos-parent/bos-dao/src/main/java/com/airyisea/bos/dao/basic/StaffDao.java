package com.airyisea.bos.dao.basic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.airyisea.bos.domain.basic.Staff;

public interface StaffDao extends JpaRepository<Staff, String>, JpaSpecificationExecutor<Staff>{

	Staff findByTelephone(String telephone);
	
	@Modifying
	@Query("update Staff set deltag = '1' where id = ?1")
	void discard(String id);
	
	@Modifying
	@Query("update Staff set deltag = '0' where id = ?1")
	void restore(String string);

}
