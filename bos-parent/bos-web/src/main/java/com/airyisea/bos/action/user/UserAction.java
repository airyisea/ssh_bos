package com.airyisea.bos.action.user;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.airyisea.bos.action.base.BaseAction;
import com.airyisea.bos.domain.user.User;
import com.airyisea.bos.utils.MD5Utils;
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
@ParentPackage("bos")
@Namespace("/user")
public class UserAction extends BaseAction<User> {
	
	//=============================ajax================================================
	//校验验证码
	@Action(value="user_checkcode",results={@Result(name="checkcode",type="json")})
	public String checkcode() {
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
	
	//修改密码
	@Action(value="user_changePassword",results={@Result(name="changePassword",type="json")})
	public String changePassword() {
		try {
			//1:修改成功,0:服务器异常,-1:密码必须为3-16位,-2:两次密码不一致,-3:与原密码一致
			String newPwd = getParameter("newPwd");
			//密码密码必须为3-16位
			if(StringUtils.isBlank(newPwd) || newPwd.length() < 3 || newPwd.length() > 16) {
				push(-1);
				return "changePassword";
			}
			//两次密码不一致
			String rePwd = getParameter("rePwd");
			if(StringUtils.isBlank(rePwd) || !newPwd.equals(rePwd)) {
				push(-2);
				return "changePassword";
			}
			User user = (User) getSessionAttribute("loginUser");
			//与原密码一致
			if(user.getPassword().equals(MD5Utils.getPwd(newPwd))) {
				push(-3);
				return "changePassword";
			}
			facadeService.getUserService().changePassword(newPwd,user.getId());
			push(1);
			return "changePassword";
		} catch (Exception e) {
			e.printStackTrace();
			push(0);
			return "changePassword";
		}
	}
	
	//=============================ajax================================================
	
	@Action(value="user_login",results={@Result(name="login_success",location="/index.jsp",type="redirect"),
										@Result(name="login_fail",location="/login.jsp"),
										@Result(name="input",location="/login.jsp")})
	public String login() {
		String ajax_code = getParameter("checkcode");
		removeSessionAttribute("checkcode");
		//验证码是否为空
		if(StringUtils.isNotBlank(ajax_code)) {
			String session_code = (String) getSessionAttribute("key");
			//验证码是否正确
			if(ajax_code.equalsIgnoreCase(session_code)) {
				//校验登录
				/*User existUser = facadeService.getUserService().login(model.getUsername(), model.getPassword());
				if(existUser != null) {
					setSessionAttribute("loginUser", existUser);
					return "login_success";
				}else {
					this.addActionError(this.getText("user.login.usernameAndPassword.error"));
					return "login_fail";
				}*/
				//使用shiro进行权限认证
				try {
					Subject subject = SecurityUtils.getSubject();
					subject.login(new UsernamePasswordToken(model.getUsername(),model.getPassword()));
					return "login_success";
				} catch (IncorrectCredentialsException e) {
					e.printStackTrace();
					this.addActionError(this.getText("user.login.password.error"));
					return "login_fail";
				} catch (UnknownAccountException e) {
					e.printStackTrace();
					this.addActionError(this.getText("user.login.username.error"));
					return "login_fail";
				}
			}
		}
		this.addFieldError("checkcodeError", this.getText("user.login.checkcode.error"));
		return "login_fail";
	}
	@Action(value="user_logout",results={@Result(name="logout",type="redirect",location="/login.jsp")})
	public String logout() throws Exception {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return "logout";
	}
	
}
