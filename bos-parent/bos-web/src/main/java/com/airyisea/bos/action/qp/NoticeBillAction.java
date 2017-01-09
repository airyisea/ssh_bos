package com.airyisea.bos.action.qp;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import com.airyisea.bos.action.base.BaseAction;
import com.airyisea.bos.domain.qp.NoticeBill;
import com.airyisea.bos.domain.qp.WorkBill;
import com.airyisea.bos.domain.user.User;
import com.airyisea.crm.domain.customer.Customer;

@SuppressWarnings("serial")
@Controller("noticeBillAction")
@Scope("prototype")
@Namespace("/qp")
@ParentPackage("bos")
public class NoticeBillAction extends BaseAction<NoticeBill> {
	
	/**
	 * 根据定区id查询客户
	 * @return
	 * @throws Exception
	 */
	@Action(value="noticebill_checkTel",results={@Result(name="checkTel",type="json")})
	public String checkTel() {
		Customer customer = facadeService.getNoticeBillService().checkTel(model.getTelephone());
		push(customer);
		return "checkTel";
	}
	
	/**
	 * 分页查询未分单通知单
	 * @return
	 * @throws Exception
	 */
	@Action(value="noticebill_findnoassociations",results={@Result(name="findnoassociations",type="fastJson",params={"includeParam","id,delegater,telephone,pickaddress,product,pickdate"})})
	public String findNoAssociations() {
		List<NoticeBill> list = facadeService.getNoticeBillService().findNoAssociations();
		push(list);
		return "findnoassociations";
	}
	
	/**
	 * 分页查询未分单通知单
	 * @return
	 * @throws Exception
	 */
	@Action(value="noticebill_createWorkBill",results={@Result(name="createWorkBill",type="json")})
	public String createWorkBill() {
		facadeService.getNoticeBillService().createManualWorkBill(model);
		push(true);
		return "createWorkBill";
	}
	
	
	@Action(value="noticebill_add",results={@Result(name="add",location="/WEB-INF/pages/qupai/noticebill_add.jsp")})
	public String add() {
		//省,市,区
		String province = getParameter("province");
		String city = getParameter("city");
		String district = getParameter("district");
		//修改model中地址
		if(province.equals(city))
			model.setPickaddress(province + district + model.getPickaddress());
		else 
			model.setPickaddress(province + city + district + model.getPickaddress());
		//封装user
		User user = (User) getSessionAttribute("loginUser");
		model.setUser(user);
		//调用业务层
		facadeService.getNoticeBillService().add(model,province,city,district);
		return "add";
	}
	
	
}
