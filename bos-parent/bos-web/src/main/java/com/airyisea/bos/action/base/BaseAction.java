package com.airyisea.bos.action.base;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.lang.reflect.*;
import java.util.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import com.airyisea.bos.domain.basic.Staff;
import com.airyisea.bos.service.facade.FacadeService;
import com.opensymphony.xwork2.*;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author Administrator
 *
 * @param <T>
 */
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
	
	//========================文件上传==============================
	protected File upload;
	protected String uploadFileName;
	protected String uploadContextType;
	
	public void setUpload(File upload) {
		this.upload = upload;
	}
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	public void setUploadContextType(String uploadContextType) {
		this.uploadContextType = uploadContextType;
	}

	//===========================分页查询============================
	//分页操作 接受页面 和 每页显示记录
	protected int page = 1;// 页码
	protected int rows = 10;// 每页显示记录数

	public void setPage(int page) {
		this.page = page;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	//向子类提供Pageable对象	
	protected Pageable getPageRequest() {
		String param = getParameter("sort");
		String order = getParameter("order");
		if(StringUtils.isNotBlank(param)&&StringUtils.isNotBlank(order))
			return new PageRequest(page - 1, rows, new Sort(Direction.fromString(order), param));
		else 
			return new PageRequest(page - 1, rows);
		//return new PageRequest(page - 1, rows);
	}
	//获取具体分页数据,由子类提供
	Page<T> pageResponse;
	protected void setPageResponse(Page<T> pageResponse) {
		this.pageResponse = pageResponse;
	}
	
	//向值栈提供页面数据
	public Map<String, Object> getPageData() {
		Map<String, Object> pageData = new HashMap<String, Object>();
		pageData.put("total", pageResponse.getTotalElements());
		pageData.put("rows", pageResponse.getContent());
		return pageData;
	}
	
	//=====================值栈操作===========================
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
	
	//=====================session操作=======================
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
	
	//==================request操作========================
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
	//===================条件查询=================================
	/**
	 * 获取进行模糊查询的Specification
	 * @param param:需要模糊查询的属性
	 * @return
	 */
	protected Specification<T> getLikeCondition(final String...params) {
		Specification<T> condition = new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				try {
					//将需要进行查询的条件装入list
					List<String> paramList = new ArrayList<String>();
					for (int i = 0; i < params.length; i++) {
						paramList.add(params[i]);
					}
					List<Predicate> plist = new ArrayList<Predicate>();
					//获取查询对象的字节码文件
					Class<? extends T> clazz = root.getJavaType();
					//内省
					BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
					PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
					//遍历属性描述器
					for (PropertyDescriptor pd : pds) {
						String name = pd.getName();
						//该属性是否要进行查询
						if(paramList.contains(name)) {
							//获取查询参数
							Object param = pd.getReadMethod().invoke(model);
							if(param != null) {
								//创建查询对象装入list中
								Predicate p = cb.like(root.get(name).as(String.class), "%" + param + "%");
								plist.add(p);
							}
						}
					}
					Predicate[] predicates = new Predicate[plist.size()];
					
					return cb.and(plist.toArray(predicates));
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};
		
		return condition;
	}
	/**
	 * 获取进行精确查询的Specification
	 * @param param:需要精确查询的属性
	 * @return
	 */
	protected Specification<T> getEqCondition(final String...params) {
		Specification<T> condition = new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				try {
					//将需要进行查询的条件装入list
					List<String> paramList = new ArrayList<String>();
					for (int i = 0; i < params.length; i++) {
						paramList.add(params[i]);
					}
					List<Predicate> plist = new ArrayList<Predicate>();
					//获取查询对象的字节码文件
					Class<? extends T> clazz = root.getJavaType();
					//内省
					BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
					PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
					//遍历属性描述器
					for (PropertyDescriptor pd : pds) {
						String name = pd.getName();
						//该属性是否要进行查询
						if(paramList.contains(name)) {
							//获取查询参数
							Object param = pd.getReadMethod().invoke(model);
							//获取属性的类型
							Class<?> returnType = pd.getReadMethod().getReturnType();
							if(param != null && !"".equals(param)) {
								//创建查询对象装入list中
								Predicate p = cb.equal(root.get(name).as(returnType), param);
								plist.add(p);
							}
						}
					}
					Predicate[] predicates = new Predicate[plist.size()];
					
					return cb.and(plist.toArray(predicates));
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};
		
		return condition;
	}
	
	protected Specification<T> getAndCondition(final Specification<T>...conditions) {
		Specification<T> condition = new Specification<T>() {
			
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				Predicate[] predicates = new Predicate[conditions.length];
				for (int i = 0; i < conditions.length; i++) {
					predicates[i] = conditions[i].toPredicate(root, query, cb);
				}
				return cb.and(predicates);
			}
		};
		return condition;
	}
}
