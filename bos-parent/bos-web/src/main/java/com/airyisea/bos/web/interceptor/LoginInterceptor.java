package com.airyisea.bos.web.interceptor;

import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;
@SuppressWarnings("serial")
@Component("loginInterceptor")
public class LoginInterceptor extends MethodFilterInterceptor {

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		
		if(invocation.getInvocationContext().getSession().get("loginUser") == null) {
			Object action = invocation.getAction();
			if(action instanceof ValidationAware) {
				ValidationAware va = (ValidationAware) action;
				if(action instanceof ActionSupport) {
					ActionSupport as = (ActionSupport) action;
					as.addActionError(as.getText("user.login.nologin"));
				}else {
					va.addActionError("登录信息已过期，请先登录!");
				}
			}
			return "no_login";
		}
		return invocation.invoke();
	}

}
