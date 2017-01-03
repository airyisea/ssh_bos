package com.airyisea.bos.dao.qp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.airyisea.bos.domain.qp.NoticeBill;

public interface NoticeBillDao extends JpaRepository<NoticeBill, String>, JpaSpecificationExecutor<NoticeBill> {

	List<NoticeBill> findByStaffIsNull();

}
