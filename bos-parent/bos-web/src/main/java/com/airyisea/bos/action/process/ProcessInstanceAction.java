package com.airyisea.bos.action.process;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.airyisea.bos.action.base.BaseAction;

@Controller("processInstanceAction")
@Scope("prototype")
@ParentPackage("bos")
@Namespace("/process")
public class ProcessInstanceAction extends BaseAction<ExecutionEntity> {
	
	private String deploymentId;
	private String imageName;

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	@Action(value="processInstance_list",
			results={@Result(name="list",location="/WEB-INF/pages/workflow/processinstance.jsp")})
	public String list() throws Exception {
		ProcessInstanceQuery query = facadeService.getRuntimeService().createProcessInstanceQuery();
		List<ProcessInstance> list = query.orderByProcessDefinitionId().asc().list();
		set("list", list);
		return "list";
	}
	
	@Action(value="processInstance_viewImage",results={@Result(name="viewImage",type="stream")})
	public String viewImage() throws Exception {
		InputStream inputStream = facadeService.getRepositoryService().getResourceAsStream(deploymentId, imageName);
		set("contentType","image/png");
		set("inputStream", inputStream);
		return "viewImage";
	}
	
	/**
	 * 查询当前任务的图片坐标，流程部署的ID，流程图片的文件名
	 * @return
	 * @throws Exception
	 */
	@Action(value="processInstance_viewpng",results={@Result(name="viewpng",location="/WEB-INF/pages/workflow/image.jsp")})
	public String viewpng() throws Exception {
		ProcessInstanceQuery query = facadeService.getRuntimeService().createProcessInstanceQuery();
		ProcessInstance processInstance = query.processInstanceId(model.getId()).singleResult();
		ProcessDefinition processDefinition = facadeService.getRepositoryService().createProcessDefinitionQuery()
											.processDefinitionId(processInstance.getProcessDefinitionId())
											.singleResult();
		deploymentId = processDefinition.getDeploymentId();
		imageName = processDefinition.getDiagramResourceName();
		
		String activityId = processInstance.getActivityId();
		ProcessDefinitionEntity pd = (ProcessDefinitionEntity) facadeService.getRepositoryService().getProcessDefinition(processInstance.getProcessDefinitionId());
		ActivityImpl activity = pd.findActivity(activityId);
		set("x", activity.getX());
		set("y", activity.getY());
		set("width", activity.getWidth());
		set("height", activity.getHeight());
		return "viewpng";
	}
	
	@Action(value="processInstance_findData")
	public String findData() throws Exception {
		Map<String, Object> variables = facadeService.getRuntimeService().getVariables(model.getId());
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("html/text;charset=utf-8");
		response.getWriter().print(variables.toString());
		return NONE;
	}
	
	/*@Action(value="processDefinition_delete",results={@Result(name="delete",type="chain",location="processDefinition_list")})
	public String delete() throws Exception {
		try {
			ProcessDefinitionQuery query = facadeService.getRepositoryService().createProcessDefinitionQuery();
			ProcessDefinition processDefinition = query.processDefinitionId(model.getId()).singleResult();
			facadeService.getRepositoryService().deleteDeployment(processDefinition.getDeploymentId());
		} catch (Exception e) {
			e.printStackTrace();
			this.set("deltag", true);
		}
		return "delete";
	}*/
	
}
