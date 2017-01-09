package com.airyisea.bos.web.result;

import java.util.Collections;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.util.TextParseUtil;
import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("serial")
@Scope("prototype")
@Component("fastJson")
public class FastJsonResult implements Result{
	private String root;
	private Set<String> includeParam = Collections.emptySet();
	private Set<String> excludeParam = Collections.emptySet();
	
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}
	//需要解析的属性
	public Set<String> getIncludeParam() {
		return includeParam;
	}

	public void setIncludeParam(String includeParam) {
		this.includeParam = TextParseUtil.commaDelimitedStringToSet(includeParam);
	}
	//不需要解析的属性
	public Set<String> getExcludeParam() {
		return excludeParam;
	}
	public void setExcludeParam(String excludeParam) {
		//struts提供的工具类，可以将字符串解析成set，字符串中必须以“,”分割
		this.excludeParam = TextParseUtil.commaDelimitedStringToSet(excludeParam);
	}


	@Override
	public void execute(ActionInvocation invocation) throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		Object object = findRootObject(invocation);
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter();
		filter.getIncludes().addAll(includeParam);
		filter.getExcludes().addAll(excludeParam);
		String string = JSON.toJSONString(object, filter);
		response.setContentType("text/json;charset=utf-8");
		response.getWriter().print(string);
	}
	
	protected Object findRootObject(ActionInvocation invocation) {
        Object rootObject;
        if (this.root != null) {
        	//以指定名称从值栈获取数据
            ValueStack stack = invocation.getStack();
            rootObject = stack.findValue(root);
            root = null;
        } else {
        	//从值栈栈顶获取数据
            rootObject = invocation.getStack().peek(); // model overrides action
        }
        return rootObject;
    }

}
