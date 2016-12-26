package com.airyisea.bos.web.result;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.util.ValueStack;

@Component("fastJson")
public class FastJsonResult implements Result{
	private String root;
	
	public String getRoot() {
		return root;
	}


	public void setRoot(String root) {
		this.root = root;
	}


	@Override
	public void execute(ActionInvocation invocation) throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		String string = JSON.toJSONString(findRootObject(invocation));
		response.setContentType("text/json;charset=utf-8");
		response.getWriter().print(string);
	}
	
	protected Object findRootObject(ActionInvocation invocation) {
        Object rootObject;
        if (this.root != null) {
            ValueStack stack = invocation.getStack();
            rootObject = stack.findValue(root);
        } else {
            rootObject = invocation.getStack().peek(); // model overrides action
        }
        return rootObject;
    }

}
