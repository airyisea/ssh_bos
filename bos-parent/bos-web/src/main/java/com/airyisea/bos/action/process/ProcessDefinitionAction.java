package com.airyisea.bos.action.process;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.airyisea.bos.action.base.BaseAction;

@Controller("processDefinitionAction")
@Scope("prototype")
@ParentPackage("bos")
@Namespace("/process")
public class ProcessDefinitionAction extends BaseAction<ProcessDefinitionEntity> {
	
	@Action(value="processDefinition_deploy",
			results={@Result(name="deploy",type="redirectAction",location="/processDefinition_list"),
					 @Result(name="deploy_fail",location="/WEB-INF/pages/workflow/processdefinition_deploy.jsp")})
	public String deploy() {
		DeploymentBuilder deployment = facadeService.getRepositoryService().createDeployment();
		ZipInputStream zipInputStream;
		try {
			zipInputStream = new ZipInputStream(new FileInputStream(upload));
			deployment.addZipInputStream(zipInputStream);
			deployment.deploy();
			return "deploy";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			this.addActionError("流程部署失败，请联系管理员");
			return "deploy_fail";
		}
	}
	
	@Action(value="processDefinition_list",
			results={@Result(name="list",location="/WEB-INF/pages/workflow/processdefinition_list.jsp")})
	public String list() throws Exception {
		ProcessDefinitionQuery query = facadeService.getRepositoryService().createProcessDefinitionQuery();
		List<ProcessDefinition> list = query.orderByProcessDefinitionName().asc().orderByProcessDefinitionVersion().desc().list();
		set("list", list);
		return "list";
	}
	
	@Action(value="processDefinition_viewpng",results={@Result(name="viewpng",type="stream")})
	public String viewpng() throws Exception {
		InputStream inputStream = facadeService.getRepositoryService().getProcessDiagram(model.getId());
		set("contentType","image/png");
		set("inputStream", inputStream);
		return "viewpng";
	}
	
	@Action(value="processDefinition_delete",results={@Result(name="delete",type="chain",location="processDefinition_list")})
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
	}
	
}
