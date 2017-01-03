package com.airyisea.bos.dao.basic;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.airyisea.bos.domain.basic.DecidedZone;
import com.airyisea.bos.domain.basic.Subarea;

public interface SubareaDao extends JpaRepository<Subarea, String>, JpaSpecificationExecutor<Subarea> {

	List<Subarea> findByDecidedZoneIsNull();
	
	@Modifying
	@Query("update Subarea set decidedZone = ?2 where id = ?1")
	void associateDecidedZone(String sid, DecidedZone model);

	List<Subarea> findByDecidedZone(DecidedZone decidedZone);
	
	@Modifying
	@Query("update Subarea set decidedZone = null where id = ?1")
	void removeDecidedZone(String id);

}
