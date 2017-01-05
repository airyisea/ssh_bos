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
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("======授权=========");
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
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
				info.addRole(role.getCode());
				Set<Function> functions = role.getFunctions();
				for (Function function : functions) {
					info.addStringPermission(function.getCode());
				}
			}
		}
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken loginToken = (UsernamePasswordToken) token;
		User existUser = facadeService.getUserService().findByUsername(loginToken.getUsername());
		if(existUser == null) {
			return null;
		}else {
			return new SimpleAuthenticationInfo(existUser, existUser.getPassword(), super.getName());
		}
	}

}
