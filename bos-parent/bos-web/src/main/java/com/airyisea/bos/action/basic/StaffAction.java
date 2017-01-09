package com.airyisea.bos.action.basic;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.airyisea.bos.action.base.BaseAction;
import com.airyisea.bos.domain.basic.Staff;
@SuppressWarnings("all")
@Controller("staffAction")
@Scope("prototype")
@Namespace("/basic")
@ParentPackage("bos")
public class StaffAction extends BaseAction<Staff> {
	
	
	//======================ajax===========================================
	
	/**
	 * 校验手机是否存在
	 * @return
	 * @throws Exception
	 */
	@Action(value="staff_checkPhone",results={@Result(name="checkPhone",type="json")})
	public String checkPhone() {
		Staff existStaff = facadeService.getStaffService().checkPhone(model.getTelephone());
		if(existStaff != null) {
			//已经存在相同标准
			push(false);
		}else {
			push(true);
		}
		return "checkPhone";
	}
	/**
	 * 分页查询收派员
	 * @return
	 * @throws Exception
	 */
	@Action(value="staff_queryPage")
	public String queryPage() {
		Map<String, Object> param = new HashMap<String, Object>();
		
		param.put("className",model.getClass().getSimpleName());
		param.put("name", model.getName());
		param.put("telephone", model.getTelephone());
		param.put("station", model.getStation());
		param.put("standard", model.getStandard());
		Specification<Staff> c1 = getEqCondition("telephone","standard");
		Specification<Staff> c2 = getLikeCondition("name","station");
		Page<Staff> pageResponse = facadeService.getStaffService().queryPage(getAndCondition(c1,c2),getPageRequest(),param);
		setPageResponse(pageResponse);
		return "queryPage";
	}
	/**
	 * 查询收派员列表
	 * @return
	 * @throws Exception
	 */
	@Action(value="staff_findListAjax",results={@Result(name="findListAjax",type="json")})
	public String findListAjax() {
		List<Staff> list = facadeService.getStaffService().findInUseList();
		push(list);
		return "findListAjax";
	}
	
	/**
	 * 批量作废收派员
	 * @return
	 * @throws Exception
	 */
	@Action(value="staff_deleteBatch",results={@Result(name="deleteBatch",type="json")})
	public String deleteBatch() {
		try {
			String ids = getParameter("ids");
			if(StringUtils.isNotBlank(ids)) {
				//将字符串ids解析成数组
				//批量删除
				facadeService.getStaffService().deleteBatch(ids.split(","));
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
	 * 批量还原收派员
	 * @return
	 * @throws Exception
	 */
	@Action(value="staff_restoreBatch",results={@Result(name="restoreBatch",type="json")})
	public String restoreBatch() {
		try {
			String ids = getParameter("ids");
			if(StringUtils.isNotBlank(ids)) {
				//将字符串ids解析成数组
				//批量删除
				facadeService.getStaffService().restoreBatch(ids.split(","));
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
		return "restoreBatch";
	}
	
	//======================ajax===========================================
	
	/**
	 * 添加收派员
	 * @return
	 * @throws Exception
	 */
	@Action(value="staff_add",results={@Result(name="add",location="/WEB-INF/pages/base/staff.jsp")})
	public String add() {
		facadeService.getStaffService().add(model);
		return "add";
	}
	
	
	/**
	 * 获取条件查询Specification的实例
	 * @return
	 */
	/*private Specification<Staff> getCondition() {
		Specification<Staff> condition = new Specification<Staff>() {
			@Override
			public Predicate toPredicate(Root<Staff> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				List<Predicate> plist = new ArrayList<>();
				String name = model.getName();
				if(StringUtils.isNotBlank(name)) {
					Predicate p1 = cb.like(root.get("name").as(String.class), "%" +name+ "%");
					plist.add(p1);
				}
				String telephone = model.getTelephone();
				if(StringUtils.isNotBlank(telephone)) {
					Predicate p2 = cb.equal(root.get("telephone").as(String.class), telephone);
					plist.add(p2);
				}
				String station = model.getStation();
				if(StringUtils.isNotBlank(station)) {
					Predicate p3 = cb.like(root.get("station").as(String.class), "%" + station + "%");
					plist.add(p3);
				}
				String standard = model.getStandard();
				if(StringUtils.isNotBlank(standard)) {
					Predicate p4 = cb.equal(root.get("standard").as(String.class), standard);
					plist.add(p4);
				}
				Predicate[] predicates = new Predicate[plist.size()];
				return cb.and(plist.toArray(predicates));
			}
			
		};
		return condition;
	}*/
	
	

}
