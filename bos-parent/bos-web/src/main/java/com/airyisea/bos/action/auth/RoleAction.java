package com.airyisea.bos.action.auth;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import com.airyisea.bos.action.base.BaseAction;
import com.airyisea.bos.domain.auth.Role;
@Controller("roleAction")
@Scope("prototype")
@Namespace("/auth")
@ParentPackage("bos")
public class RoleAction extends BaseAction<Role> {
	
	/**
	 * 查询所有角色列表
	 * @return
	 * @throws Exception
	 */
	@Action(value="role_findListAll",results={@Result(name="findListAll",type="fastJson",params={"includeParam","id,name"})})
	public String findListAll() {
		List<Role> list = facadeService.getRoleService().findAll();
		push(list);
		return "findListAll";
	}
	
	/**
	 * 校验关键字
	 * @return
	 * @throws Exception
	 */
	@Action(value="role_checkCode",results={@Result(name="checkCode",type="json")})
	public String checkCode() {
		Role existFunction = facadeService.getRoleService().findByCode(model.getCode());
		if(existFunction == null) {
			push(true);
		}else {
			push(false);
		}
		return "checkCode";
	}
	
	/**
	 * 分页查询角色
	 * @return
	 * @throws Exception
	 */
	@Action(value="role_queryPage",results={
			@Result(name="queryPage",type="fastJson",params={"root","pageData","excludeParam","code,users,functions"})})
	public String queryPage() {
		Page<Role> pageResponse = facadeService.getRoleService().queryPage(getPageRequest());
		setPageResponse(pageResponse);
		return "queryPage";
	}
	
	
	
	/**
	 * 添加角色
	 * @return
	 * @throws Exception
	 */
	@Action(value="role_add",results={@Result(name="add",location="/WEB-INF/pages/admin/role.jsp")})
	public String add() {
		String fids = getParameter("fids");
		facadeService.getRoleService().add(model,fids);
		return "add";
	}
	
}
