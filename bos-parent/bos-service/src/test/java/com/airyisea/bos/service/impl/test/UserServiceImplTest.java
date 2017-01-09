package com.airyisea.bos.service.impl.test;

import org.activiti.engine.RepositoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.airyisea.bos.dao.user.DemoDao;
import com.airyisea.bos.domain.user.User;
import com.airyisea.bos.service.facade.FacadeService;
import com.airyisea.bos.service.user.UserService;
import com.airyisea.bos.utils.MD5Utils;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value={"classpath:applicationContext-domain.xml", 
							"classpath:applicationContext-dao.xml",
							"classpath:applicationContext-service.xml" })
public class UserServiceImplTest {
	@Autowired
	private UserService userService;
	@Autowired
	private DemoDao demoDao;
	@Autowired
	private RepositoryService repositoryService;
	
	
	@Test
	public void testSave() {
		User user = new User("张三", "123456");
		userService.save(user);
		//int i = 1/0;
		System.out.println(user);
	}
	@Test
	public void testFindById() {
		User user = userService.findOne(1);
		System.out.println(user);
	}

	@Test
	public void testLogin1() {
		User user = demoDao.login("张三", MD5Utils.getPwd("123"));
		System.out.println(user);
	}
	
	@Test
	public void testLogin2() {
		User user = demoDao.login2("张三", MD5Utils.getPwd("123"));
		System.out.println(user);
	}
	@Test
	public void testLogin3() {
		User user = demoDao.login3("张三", MD5Utils.getPwd("123"));
		System.out.println(user);
	}
	@Test
	public void testLogin4() {
		User user = demoDao.login4("张三", MD5Utils.getPwd("123"));
		System.out.println(user);
	}
	@Test
	public void delete() {
		repositoryService.deleteDeployment("1");
	}
	
	

}
