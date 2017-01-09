package com.airyisea.bos.action.process;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.airyisea.bos.action.base.BaseAction;
import com.airyisea.bos.domain.user.User;

@Controller("taskAction")
@Scope("prototype")
@ParentPackage("bos")
@Namespace("/process")
public class TaskAction extends BaseAction<TaskEntity> {

	@Action(value="task_groupTaskList",
			results={@Result(name="groupTaskList",location="/WEB-INF/pages/workflow/grouptask.jsp")})
	public String groupTaskList() throws Exception {
		TaskQuery query = facadeService.getTaskService().createTaskQuery();
		User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
		List<Task> list = query.taskCandidateUser(loginUser.getId() + "").list();
		set("list", list);
		return "groupTaskList";
	}
	@Action(value="task_personalTaskList",
			results={@Result(name="personalTaskList",location="/WEB-INF/pages/workflow/personaltask.jsp")})
	public String personalTaskList() throws Exception {
		TaskQuery query = facadeService.getTaskService().createTaskQuery();
		User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
		List<Task> list = query.taskAssignee(loginUser.getId() + "").list();
		set("list", list);
		return "personalTaskList";
	}
	
	
	@Action(value="task_showData")
	public String findData() throws Exception {
		Map<String, Object> variables = facadeService.getTaskService().getVariables(model.getId());
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("html/text;charset=utf-8");
		response.getWriter().print(variables.toString());
		return NONE;
	}
	@Action(value="task_takeTask",results={@Result(name="takeTask",type="redirectAction",location="task_groupTaskList")})
	public String takeTask() throws Exception {
		User loginUser = (User) SecurityUtils.getSubject().getPrincipal();
		facadeService.getTaskService().claim(model.getId(), loginUser.getId() + "");
		return "takeTask";
	}
	@Action(value="task_checkWorkOrderManage",results={@Result(name="goCheck",location="/WEB-INF/pages/workflow/check.jsp"),
													   @Result(name="checkWorkOrderManage",type="redirectAction",location="task_personalTaskList")})
	public String checkWorkOrderManage() throws Exception {
		String check = getParameter("check");
		if(StringUtils.isBlank(check)) {
			set("map", facadeService.getTaskService().getVariables(model.getId()));
			return "goCheck";
		}else {
			facadeService.getWorkOrderManageService().check(model.getId(),check);
			return "checkWorkOrderManage";
		}
	}
	
	@Action(value="task_outStore",results={@Result(name="outStore",type="redirectAction",location="task_personalTaskList")})
	public String outStore() throws Exception {
		facadeService.getTaskService().complete(model.getId());
		return "outStore";
	}
	
	@Action(value="task_transferGoods",results={@Result(name="transferGoods",type="redirectAction",location="task_personalTaskList")})
	public String transferGoods() throws Exception {
		facadeService.getTaskService().complete(model.getId());
		return "transferGoods";
	}
	
	@Action(value="task_receive",results={@Result(name="receive",type="redirectAction",location="task_personalTaskList")})
	public String receive() throws Exception {
		facadeService.getTaskService().complete(model.getId());
		return "receive";
	}
	
	
}
