package com.airyisea.bos.dao.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.airyisea.bos.domain.auth.Function;

public interface FunctionDao extends JpaRepository<Function, String>,JpaSpecificationExecutor<Function>{
	@Query("from Function where generatemenu = '1'")
	List<Function> findParentList();
	
	@Query("from Function f left join fetch f.roles r left join fetch r.users u where u.id = ?1 order by zindex")
	List<Function> findByUserId(Integer id);

}
