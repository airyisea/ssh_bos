package com.airyisea.bos.action.user;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.airyisea.bos.domain.user.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
@Controller("demoAction")
@Scope("prototype")
@Namespace("/")
@ParentPackage("bos")
public class DemoAction extends ActionSupport {
	
	@Action(value="fast")
	public String execute() throws Exception {
		User user = new User();
		user.setId(1);
		user.setUsername("张三");
		//ActionContext.getContext().getValueStack().push(user);
		ActionContext.getContext().getValueStack().set("test", user);
		return "fast";
	}
}
