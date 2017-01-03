package com.airyisea.bos.action.basic;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.airyisea.bos.action.base.BaseAction;
import com.airyisea.bos.domain.basic.Standard;
import com.airyisea.bos.domain.user.User;
@SuppressWarnings("serial")
@Controller("standardAction")
@Scope("prototype")
@Namespace("/basic")
@ParentPackage("bos")
public class StandardAction extends BaseAction<Standard> {
	
	
	//======================ajax===========================================
	
	/**
	 * 校验收派标准是否存在
	 * @return
	 * @throws Exception
	 */
	@Action(value="standard_checkName",results={@Result(name="checkName",type="json")})
	public String checkName() throws Exception {
		Standard existStandard = facadeService.getStandardService().checkName(model.getName());
		if(existStandard != null) {
			//已经存在相同标准
			push(false);
		}else {
			push(true);
		}
		return "checkName";
	}
	
	/**
	 * 分页查询收派标准记录
	 * @return
	 * @throws Exception
	 */
	@Action(value="standard_queryPage",results={@Result(name="queryPage",type="json")})
	public String queryPage() throws Exception {
		Map<String, Object> pageData = new HashMap<String, Object>();
		Pageable pageable = new PageRequest(page - 1, rows);
		//获取page对象
		Page<Standard> page = facadeService.getStandardService().queryPage(pageable);
		//将总记录与本页数据封装入map
		pageData.put("total", page.getTotalElements());
		pageData.put("rows", page.getContent());
		push(pageData);
		return "queryPage";
	}
	
	/**
	 * 批量删除数据
	 * @return
	 * @throws Exception
	 */
	@Action(value="standard_deleteBatch",results={@Result(name="deleteBatch",type="json")})
	public String deleteBatch() throws Exception {
		try {
			String ids = getParameter("ids");
			if(StringUtils.isNotBlank(ids)) {
				//将字符串ids解析成数组
				//批量删除
				facadeService.getStandardService().deleteBatch(ids.split(","));
				push(true);
			}else {
				//非法数据
				push(false);
			}
		} catch (Exception e) {
			//数据库异常
			e.printStackTrace();
			push(false);
		}
		return "deleteBatch";
	}
	
	/**
	 * 获取收派标准列表
	 * @return
	 * @throws Exception
	 */
	@Action(value="standard_nameList",results={@Result(name="nameList",type="json")})
	public String nameList() throws Exception {
		List<Standard> list = facadeService.getStandardService().findAll();
		push(list);
		return "nameList";
	}
	
	//======================ajax===========================================
	
	/**
	 * 添加收派标准
	 * @return
	 * @throws Exception
	 */
	@Action(value="standard_add",results={@Result(name="add",location="/WEB-INF/pages/base/standard.jsp")})
	public String add() throws Exception {
		User loginUser = (User) getSessionAttribute("loginUser");
		//封装其他参数
		model.setOperator(loginUser.getUsername());
		model.setOperatorStation(loginUser.getStation());
		model.setOperatingTime(new Date());
		facadeService.getStandardService().add(model);
		return "add";
	}

}
