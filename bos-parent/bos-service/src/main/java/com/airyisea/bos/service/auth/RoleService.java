package com.airyisea.bos.service.auth;

import java.util.List;

import com.airyisea.bos.domain.auth.Role;
import com.airyisea.bos.service.base.BaseService;

public interface RoleService extends BaseService<Role, String>{

	List<Role> findByUserId(Integer id);

	Role findByCode(String code);

	void add(Role model, String fids);

}
