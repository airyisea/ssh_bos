package com.airyisea.bos.action.user;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.airyisea.bos.action.base.BaseAction;
import com.airyisea.bos.domain.user.User;
@Controller
@Scope("prototype")
@ParentPackage("bos")
@Namespace("/user")
public class UserAction extends BaseAction<User> {
	
	@Action(value="user_checkcode",results={@Result(name="checkcode",type="json")})
	public String checkcode() throws Exception {
		String ajax_code = getParameter("checkcode");
		String session_code = (String) getSessionAttribute("key");
		boolean checkcode_flag = false;
		if(StringUtils.isNotBlank(ajax_code)) {
			if(ajax_code.equalsIgnoreCase(session_code)) {
				checkcode_flag = true;
			}
		}
		push(checkcode_flag);
		return "checkcode";
	}
	
	@Action(value="user_login",results={@Result(name="login_success",location="/index.jsp",type="redirect"),
										@Result(name="login_fail",location="/login.jsp")})
	public String login() throws Exception {
		String ajax_code = getParameter("checkcode");
		removeSessionAttribute("checkcode");
		//验证码是否为空
		if(StringUtils.isNotBlank(ajax_code)) {
			String session_code = (String) getSessionAttribute("key");
			//验证码是否正确
			if(ajax_code.equalsIgnoreCase(session_code)) {
				//校验登录
				User existUser = facadeService.getUserService().login(model.getUsername(), model.getPassword());
				if(existUser != null) {
					setSessionAttribute("loginUser", existUser);
					return "login_success";
				}else {
					this.addActionError(this.getText("user.login.usernameAndPassword.error"));
					return "login_fail";
				}
			}
		}
		this.addFieldError("checkcodeError", this.getText("user.login.checkcode.error"));
		return "login_fail";
	}
	
}
