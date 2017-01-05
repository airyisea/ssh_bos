package com.airyisea.bos.action.auth;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.airyisea.bos.action.base.BaseAction;
import com.airyisea.bos.domain.auth.Function;
import com.airyisea.bos.domain.basic.Staff;
import com.airyisea.bos.domain.user.User;
@Controller("functionAction")
@Scope("prototype")
@Namespace("/auth")
@ParentPackage("bos")
public class FunctionAction extends BaseAction<Function> {
	
	/**
	 * 查询父权限列表
	 * @return
	 * @throws Exception
	 */
	@Action(value="function_findParentList",results={@Result(name="findParentList",type="json")})
	public String findParentList() {
		List<Function> list = facadeService.getFunctionService().findParentList();
		push(list);
		return "findParentList";
	}
	/**
	 * 查询所有权限列表
	 * @return
	 * @throws Exception
	 */
	@Action(value="function_findListAjax",results={@Result(name="findListAjax",type="json")})
	public String findListAjax() {
		List<Function> list = facadeService.getFunctionService().findAll();
		push(list);
		return "findListAjax";
	}
	/**
	 * 查询用户的权限
	 * @return
	 * @throws Exception
	 */
	@Action(value="function_findByUser",results={@Result(name="findByUser",type="json")})
	public String findByUser() {
		Subject subject = SecurityUtils.getSubject();
		User loginUser = (User) subject.getPrincipal();
		List<Function> list = facadeService.getFunctionService().findByUser(loginUser);
		push(list);
		return "findByUser";
	}
	/**
	 * 校验id
	 * @return
	 * @throws Exception
	 */
	@Action(value="function_checkId",results={@Result(name="checkId",type="json")})
	public String checkId() {
		Function existFunction = facadeService.getFunctionService().findOne(model.getId());
		if(existFunction == null) {
			push(true);
		}else {
			push(false);
		}
		return "checkId";
	}
	
	/**
	 * 分页查询
	 * @return
	 * @throws Exception
	 */
	@Action(value="function_queryPage")
	public String queryPage() {
		setPage(Integer.parseInt(getParameter("page")));
		Page<Function> pageResponse = facadeService.getFunctionService().queryPage(getPageRequest());
		setPageResponse(pageResponse);
		return "queryPage";
	}
	
	
	/**
	 * 添加权限
	 * @return
	 * @throws Exception
	 */
	@Action(value="function_add",results={@Result(name="add",location="/WEB-INF/pages/admin/function.jsp")})
	public String add() {
		if(StringUtils.isBlank(model.getFunction().getId())) {
			model.setFunction(null);
		}
		facadeService.getFunctionService().add(model);
		return "add";
	}
	
}
