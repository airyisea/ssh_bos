package com.airyisea.bos.dao.basic;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.airyisea.bos.domain.basic.Region;

public interface RegionDao extends JpaRepository<Region, String>, JpaSpecificationExecutor<Region>{

	List<Region> findByProvinceLikeOrCityLikeOrDistrictLike(String param, String param2, String param3);
	
	@Query("from Region where province like ?1 or city like ?1 or district like ?1")
	List<Region> findByProvinceOrCityOrDistrict(String string);
	
	@Query("select district from Region where province = ?1 and city = ?2")
	List<String> findDistrictByProvinceAndCity(String province, String city);
	
	@Query("select distinct city from Region where province = ?1")
	List<String> findCityByProvince(String province);
	
	@Query("select distinct province from Region")
	List<String> findProvince();
	
	Region findByProvinceAndCityAndDistrictLike(String province, String city,String district);

}
