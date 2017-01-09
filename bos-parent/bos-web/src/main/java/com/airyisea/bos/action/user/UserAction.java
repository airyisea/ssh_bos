package com.airyisea.bos.action.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
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
import com.airyisea.bos.domain.basic.Staff;
import com.airyisea.bos.domain.user.User;
import com.airyisea.bos.utils.MD5Utils;
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
@ParentPackage("bos")
@Namespace("/user")
public class UserAction extends BaseAction<User> {
	
	private Date _birthday2;
	public Date get_birthday2() {
		return _birthday2;
	}
	public void set_birthday2(Date _birthday2) {
		this._birthday2 = _birthday2;
	}
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
	/**
	 * 校验手机是否存在
	 * @return
	 * @throws Exception
	 */
	@Action(value="user_checkPhone",results={@Result(name="checkPhone",type="json")})
	public String checkPhone() {
		User existUser = facadeService.getUserService().findByPhone(model.getTelephone());
		if(existUser != null) {
			push(false);
		}else {
			push(true);
		}
		return "checkPhone";
	}
	/**
	 * 校验用户名是否存在
	 * @return
	 * @throws Exception
	 */
	@Action(value="user_checkName",results={@Result(name="checkName",type="json")})
	public String checkName() {
		User existUser = facadeService.getUserService().findByUsername(model.getUsername());
		if(existUser != null) {
			push(false);
		}else {
			push(true);
		}
		return "checkName";
	}
	
	/**
	 * 分页查询用户
	 * @return
	 * @throws Exception
	 */
	@Action(value="user_queryPage",results={
			@Result(name="queryPage",type="fastJson",params={"root","pageData","excludeParam","password,remark,roles,noticeBills"})})
	public String queryPage() {
		Page<User> pageResponse = facadeService.getUserService().queryPage(getCondition(), getPageRequest());
		setPageResponse(pageResponse);
		return "queryPage";
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
	
	/**
	 * 添加用户
	 * @return
	 * @throws Exception
	 */
	@Action(value="user_add",results={@Result(name="add",location="/WEB-INF/pages/admin/userlist.jsp")})
	public String add() {
		String[] rids = getRequest().getParameterValues("rid");
		facadeService.getUserService().add(model,rids);
		return "add";
	}
	
	private Specification<User> getCondition() {
		Specification<User> condition = new Specification<User>() {
			
			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				List<Predicate> plist = new ArrayList<>();
				String username = model.getUsername();
				if(StringUtils.isNotBlank(username)) {
					Predicate p1 = cb.like(root.get("username").as(String.class), "%" +username+ "%");
					plist.add(p1);
				}
				String gender = model.getGender();
				if(StringUtils.isNotBlank(gender)) {
					Predicate p2 = cb.equal(root.get("gender").as(String.class), gender);
					plist.add(p2);
				}
				Date birthday = model.getBirthday();
				if(birthday != null) {
					Predicate p3 = cb.greaterThanOrEqualTo(root.get("birthday").as(Date.class), birthday);
					plist.add(p3);
				}
				if(_birthday2 != null) {
					Predicate p4 = cb.lessThanOrEqualTo(root.get("birthday").as(Date.class), _birthday2);
					plist.add(p4);
				}
				Predicate[] predicates = new Predicate[plist.size()];
				return cb.and(plist.toArray(predicates));
			}
		};
		return condition;
	}
	
	
}
