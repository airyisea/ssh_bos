package com.airyisea.bos.action.qp;

import java.sql.Date;
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
import com.airyisea.bos.domain.qp.NoticeBill;
import com.airyisea.bos.domain.qp.WorkBill;
import com.airyisea.crm.domain.customer.Customer;

@SuppressWarnings("serial")
@Controller("workBillAction")
@Scope("prototype")
@Namespace("/qp")
@ParentPackage("bos")
public class WorkBillAction extends BaseAction<WorkBill> {
	
	/**
	 * 条件分页查询
	 * @return
	 * @throws Exception
	 */
	@Action(value="workbill_queryPage")
	public String queryPage() {
		Page<WorkBill> pageResponse = facadeService.getWorkBillService().queryPage(getCondition(),getPageRequest());
		setPageResponse(pageResponse);
		return "queryPage";
	}
	
	
	@Action(value="workbill_repeat",results={@Result(name="repeat",type="json")})
	public String repeat() {
		String param = getParameter("ids");
		if(StringUtils.isNotBlank(param)) {
			facadeService.getWorkBillService().repeat(param);
			push(true);
		}else {
			push(false);
		}
		return "repeat";
	}
	@Action(value="workbill_cancel",results={@Result(name="cancel",type="json")})
	public String cancel() {
		String param = getParameter("ids");
		if(StringUtils.isNotBlank(param)) {
			facadeService.getWorkBillService().cancel(param);
			push(true);
		}else {
			push(false);
		}
		return "cancel";
	}
	
	/**
	 * 修改定区关联客户
	 * @return
	 * @throws Exception
	 */
/*	@Action(value="decidedZone_assignCustomersToDecidedzone",results={@Result(name="assignCustomersToDecidedzone",location="/WEB-INF/pages/base/decidedzone.jsp")})
	public String assignCustomersToDecidedzone() {
		String[] cids = getRequest().getParameterValues("customerIds");
		facadeService.getDecidedZoneService().assignCustomersToDecidedzone(cids,model.getId());
		return "assignCustomersToDecidedzone";
	}*/
	
	
	//=========================================================================
	/**
	 * 获取条件查询对象
	 * @return
	 */
	private Specification<WorkBill> getCondition() {
		Specification<WorkBill> condition = new Specification<WorkBill>() {
			@Override
			public Predicate toPredicate(Root<WorkBill> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				List<Predicate> plist = new ArrayList<>();
				//受理时间
				if(model.getBuildtime() != null) {
					Predicate p1 = cb.equal(root.get("buildtime").as(Date.class), model.getBuildtime());
					plist.add(p1);
				}
				//客户电话
				if(model.getNoticeBill() != null && StringUtils.isNotBlank(model.getNoticeBill().getTelephone())) {
					Join<WorkBill, NoticeBill> join = root.join(root.getModel().getSingularAttribute("noticeBill",NoticeBill.class), JoinType.LEFT);
					Predicate p2 = cb.equal(join.get("telephone").as(String.class), model.getNoticeBill().getTelephone());
					plist.add(p2);
				}
				//取派员
				if(model.getStaff() != null && StringUtils.isNotBlank(model.getStaff().getName())) {
					Join<WorkBill, Staff> join = root.join(root.getModel().getSingularAttribute("staff",Staff.class), JoinType.LEFT);
					Predicate p3 = cb.like(join.get("name").as(String.class), "%" + model.getStaff().getName()+ "%");
					plist.add(p3);
				}
				Predicate[] predicates = new Predicate[plist.size()];
				return cb.and(plist.toArray(predicates));
			}
		};
		return condition;
	}
}
