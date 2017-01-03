package com.airyisea.bos.action.qp;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import com.airyisea.bos.action.base.BaseAction;
import com.airyisea.bos.domain.qp.WorkBill;
import com.airyisea.bos.domain.qp.WorkOrderManage;
import com.airyisea.crm.domain.customer.Customer;

@SuppressWarnings("serial")
@Controller("workOrderManageAction")
@Scope("prototype")
@Namespace("/qp")
@ParentPackage("bos")
public class WorkOrderManageAction extends BaseAction<WorkOrderManage> {
	
	
	@Action(value="workordermanage_add",results={@Result(name="add",type="json")})
	public String add() {
		try {
			facadeService.getWorkOrderManageService().add(model);
			push(true);
		} catch (Exception e) {
			push(false);
			e.printStackTrace();
		}
		return "add";
	}
	
	@Action(value="workordermanage_queryPage")
	public String queryPage() {
		String conditionName = getParameter("conditionName");
		String conditionValue = getParameter("conditionValue");
		Page<WorkOrderManage> pageResponse = null;
		if(StringUtils.isNotBlank(conditionValue) && StringUtils.isNotBlank(conditionName)) {
			pageResponse = facadeService.getWorkOrderManageService().queryPage(getPageRequest(), conditionName, conditionValue);
		}else {
			pageResponse = facadeService.getWorkOrderManageService().queryPage(getPageRequest());
			
		}
		setPageResponse(pageResponse);
		return "queryPage";
	}
}
