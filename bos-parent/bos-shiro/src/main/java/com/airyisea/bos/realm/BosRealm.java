package com.airyisea.bos.realm;

import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.airyisea.bos.domain.auth.Function;
import com.airyisea.bos.domain.auth.Role;
import com.airyisea.bos.domain.user.User;
import com.airyisea.bos.service.facade.FacadeService;

public class BosRealm extends AuthorizingRealm{
	
	@Autowired
	private FacadeService facadeService;
	@Override	
	//进行授权的方法
	//如果返回null，说明没有权限，shiro会自动跳到<property name="unauthorizedUrl" value="/unauthorized.jsp" />
	//如果不返回null，根据配置/page_base_subarea.action = roles["base"]，去自动匹配
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		//根据当前登录用户查询用户对应的角色
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		//或者使用这种方式也可以获得当前用户
		//User user =(User)principals.getPrimaryPrincipal();
		//admin用户是超级管理员，赋予全部角色和权限
		if("admin".equals(user.getUsername())) {
			List<Role> rlist = facadeService.getRoleService().findAll();
			for (Role role : rlist) {
				info.addRole(role.getCode());
			}
			List<Function> flist = facadeService.getFunctionService().findAll();
			for (Function function : flist) {
				info.addStringPermission(function.getCode());
			}
		}else {
			List<Role> rlist = facadeService.getRoleService().findByUserId(user.getId());
			for (Role role : rlist) {
				//添加当前用户所拥有的角色
				info.addRole(role.getCode());
				//获取当前角色所拥有的权限(导航查询)
				Set<Function> functions = role.getFunctions();
				for (Function function : functions) {
					//添加当前用户拥有的权限
					info.addStringPermission(function.getCode());
				}
			}
		}
		return info;
	}
	@Override
	//进行认证的方法，如果返回的是null抛出用户不存在的异常UnknownAccountException
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//用户名密码令牌（action传过来）
		UsernamePasswordToken loginToken = (UsernamePasswordToken) token;
		//调用业务层来查询(根据用户名来查询用户，无需密码)
		User existUser = facadeService.getUserService().findByUsername(loginToken.getUsername());
		if(existUser == null) {
			//用户名不存在，会自动抛出异常
			return null;
		}else {
			//参数1：用户对象，将来要放入session,数据库查询出来的用户
			//参数2：凭证（密码）：密码校验：校验的动作交给shiro，如果密码错误，会自动抛出IncorrectCredentialsException
			//参数3:当前使用的Realm在Spring容器中的名字(bean的名字，自动在spring容器中寻找)
			return new SimpleAuthenticationInfo(existUser, existUser.getPassword(), super.getName());
		}
	}

}
