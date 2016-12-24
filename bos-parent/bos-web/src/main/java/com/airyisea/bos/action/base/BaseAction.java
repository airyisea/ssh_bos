package com.airyisea.bos.action.base;

import java.lang.reflect.*;
import java.util.*;

import javax.servlet.http.*;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.airyisea.bos.service.facade.FacadeService;
import com.opensymphony.xwork2.*;
import com.opensymphony.xwork2.util.ValueStack;

@SuppressWarnings("all")
public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T> {
	
	public BaseAction() {
		try {
			Type type = this.getClass().getGenericSuperclass();
			ParameterizedType pt = (ParameterizedType) type;
			Class c = (Class) pt.getActualTypeArguments()[0];
			model = (T) c.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	protected T model;
	
	@Override
	public T getModel() {
		return model;
	}
	
	//门面Service
	@Autowired
	protected FacadeService facadeService;
	
	//值栈操作
	//获取值栈
	protected ValueStack getValueStack() {
		return ActionContext.getContext().getValueStack();
	}
	//压入栈顶
	protected void push(Object obj) {
		getValueStack().push(obj);
	}
	//以命名形式压入栈顶
	protected void set(String key, Object obj) {
		getValueStack().set(key, obj);
	}
	//压入map栈
	protected void put(String key, Object obj) {
		ActionContext.getContext().put(key, obj);
	}
	
	//session操作
	protected HttpSession getSession() {
		return ServletActionContext.getRequest().getSession();
	}
	
	protected Object getSessionAttribute(String key) {
		return ActionContext.getContext().getSession().get(key);
	}
	
	protected void setSessionAttribute(String key, Object obj) {
		ActionContext.getContext().getSession().put(key, obj);
	}
	
	protected void removeSessionAttribute(String key) {
		ActionContext.getContext().getSession().remove(key);
	}
	
	//request操作
	protected HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}
	
	protected String getParameter(String key) {
		return getRequest().getParameter(key);
	}
	
	protected String getParamFromMap(String param,Map<String,String[]> paramMap){
		if(paramMap == null || paramMap.isEmpty()) {
			return null;
		}else {
			for(String key : paramMap.keySet()) {
				if(key.equalsIgnoreCase(param)) {
					return paramMap.get(key)[0];
				}
			}
		}
		return null;
	}
	
	// 分页操作 接受页面 和 每页显示记录
	protected int page = 1;// 页码
	protected int rows = 10;// 每页显示记录数

	public void setPage(int page) {
		this.page = page;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	

}
