package com.airyisea.bos.dao.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.airyisea.bos.domain.auth.Role;

public interface RoleDao extends JpaRepository<Role, String>,JpaSpecificationExecutor<Role>{
	
	@Query("from Role r left join fetch r.users u where u.id = ?1")
	List<Role> findByUserId(Integer uid);

	Role findByCode(String code);

}
