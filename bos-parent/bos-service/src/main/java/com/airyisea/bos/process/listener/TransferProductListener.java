package com.airyisea.bos.process.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.airyisea.bos.service.facade.FacadeService;

public class TransferProductListener implements TaskListener {
	@Override
	public void notify(DelegateTask delegateTask) {
		String assignee = delegateTask.getAssignee();
		delegateTask.setVariable("staff", assignee);
	}

}
