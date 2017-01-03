package com.airyisea.bos.dao.qp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.airyisea.bos.domain.qp.WorkBill;

public interface WorkBillDao extends JpaRepository<WorkBill, String>, JpaSpecificationExecutor<WorkBill> {
	
	@Modifying
	@Query("update WorkBill set attachbilltimes = attachbilltimes + 1 where id = ?1")
	void addAttachbilltimes(String id);

	@Modifying
	@Query("update WorkBill set type = '销', pickstate='已注销' where id = ?1")
	void cancel(String id);

}
