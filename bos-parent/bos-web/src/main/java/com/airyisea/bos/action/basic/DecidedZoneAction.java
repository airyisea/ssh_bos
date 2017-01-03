package com.airyisea.bos.action.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
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
import com.airyisea.bos.domain.basic.DecidedZone;
import com.airyisea.bos.domain.basic.Staff;
import com.airyisea.crm.domain.customer.Customer;

@SuppressWarnings("serial")
@Controller("decidedZoneAction")
@Scope("prototype")
@Namespace("/basic")
@ParentPackage("bos")
public class DecidedZoneAction extends BaseAction<DecidedZone> {
	
	/**
	 * 校验id是否重复
	 * @return
	 * @throws Exception
	 */
	@Action(value="decidedZone_checkId",results={@Result(name="checkId",type="json")})
	public String checkId() {
		DecidedZone existDZ = facadeService.getDecidedZoneService().checkId(model.getId());
		if(existDZ == null) {
			push(true);
		}else {
			push(false);
		}
		return "checkId";
	}
	/**
	 * 条件分页查询
	 * @return
	 * @throws Exception
	 */
	@Action(value="decidedZone_queryPage")
	public String queryPage() {
		Page<DecidedZone> pageResponse = facadeService.getDecidedZoneService().queryPage(getCondition(),getPageRequest());
		setPageResponse(pageResponse);
		return "queryPage";
	}
	/**
	 * 查询还没定区的客户
	 * @return
	 * @throws Exception
	 */
	@Action(value="decidedZone_getNoAssociationCustomer",results={@Result(name="getNoAssociationCustomer",type="json")})
	public String getNoAssociationCustomer() {
		List<Customer> list = facadeService.getDecidedZoneService().getNoAssociationCustomer();
		push(list);
		return "getNoAssociationCustomer";
	}
	/**
	 * 根据定区id查询客户
	 * @return
	 * @throws Exception
	 */
	@Action(value="decidedZone_getAssociationCustomer",results={@Result(name="getAssociationCustomer",type="json")})
	public String getAssociationCustomer() {
		List<Customer> list = facadeService.getDecidedZoneService().getAssociationCustomer(model.getId());
		push(list);
		return "getAssociationCustomer";
	}
	
	
	//=======================================================================
	/**
	 * 添加定区
	 * @return
	 * @throws Exception
	 */
	@Action(value="decidedZone_add",results={@Result(name="add",location="/WEB-INF/pages/base/decidedzone.jsp")})
	public String add() {
		String[] subareas = getRequest().getParameterValues("subareaId");
		facadeService.getDecidedZoneService().add(model,subareas);
		return "add";
	}
	/**
	 * 修改定区关联客户
	 * @return
	 * @throws Exception
	 */
	@Action(value="decidedZone_assignCustomersToDecidedzone",results={@Result(name="assignCustomersToDecidedzone",location="/WEB-INF/pages/base/decidedzone.jsp")})
	public String assignCustomersToDecidedzone() {
		String[] cids = getRequest().getParameterValues("customerIds");
		facadeService.getDecidedZoneService().assignCustomersToDecidedzone(cids,model.getId());
		return "assignCustomersToDecidedzone";
	}
	
	
	//=========================================================================
	/**
	 * 获取条件查询对象
	 * @return
	 */
	private Specification<DecidedZone> getCondition() {
		Specification<DecidedZone> condition = new Specification<DecidedZone>() {
			@Override
			public Predicate toPredicate(Root<DecidedZone> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				List<Predicate> plist = new ArrayList<>();
				if(StringUtils.isNotBlank(model.getId())) {
					Predicate p1 = cb.equal(root.get("id").as(String.class), model.getId());
					plist.add(p1);
				}
				if(model.getStaff() != null && StringUtils.isNotBlank(model.getStaff().getStation())) {
					Join<DecidedZone, Staff> join = root.join(root.getModel().getSingularAttribute("staff",Staff.class),JoinType.LEFT);
					Predicate p2 = cb.like(join.get("station").as(String.class), model.getStaff().getStation());
					plist.add(p2);
				}
				if("1".equals(getParameter("isAssociation"))) {
					Predicate p3 = cb.isNotEmpty(root.get("subareas").as(Set.class));
					plist.add(p3);
				}else if("0".equals(getParameter("isAssociation"))) {
					Predicate p3 = cb.isEmpty(root.get("subareas").as(Set.class));
					plist.add(p3);
				}
				Predicate[] predicates = new Predicate[plist.size()];
				return cb.and(plist.toArray(predicates));
			}
		};
		return condition;
	}
}
