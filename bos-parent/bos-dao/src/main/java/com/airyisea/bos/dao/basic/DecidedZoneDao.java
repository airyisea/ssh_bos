package com.airyisea.bos.dao.basic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.airyisea.bos.domain.basic.DecidedZone;
import com.airyisea.bos.domain.basic.Staff;

public interface DecidedZoneDao extends JpaRepository<DecidedZone, String>, JpaSpecificationExecutor<DecidedZone> {
	@Modifying
	@Query("update DecidedZone set staff = null where staff = ?1")
	void removeStaff(Staff staff);

}
