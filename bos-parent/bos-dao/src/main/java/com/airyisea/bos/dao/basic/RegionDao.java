package com.airyisea.bos.dao.basic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.airyisea.bos.domain.basic.Region;

public interface RegionDao extends JpaRepository<Region, String>, JpaSpecificationExecutor<Region>{

}
