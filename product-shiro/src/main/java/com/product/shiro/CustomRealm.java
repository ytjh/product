package com.product.shiro;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;


public class CustomRealm  extends AuthorizingRealm {
	
	//注入service
	/*@Autowired
	private UserService userService;
	
	@Autowired
	private PermissionService permissionService;*/
	// 设置realm的名称
	@Override
	public void setName(String name) {
		super.setName("customRealm");
	}
	//realm的认证方法，从数据库查询用户信息
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
				return null;
		/*
		// token是用户输入的用户名和密码 
		// 第一步从token中取出用户名
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String name=token.getUsername();

		// 第二步：根据用户输入的userCode从数据库查询
		User user=null;
		try {
			user = userService.getByName(name);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 如果查询不到返回null
		if(user==null){//
			return null;
		}
		// 从数据库查询到密码
		String password = user.getPassword();
		
	

		// 如果查询到返回认证信息AuthenticationInfo
		
		//activeUser就是用户身份信息
		ActiveUser activeUser = new ActiveUser();
		
		activeUser.setUserid(user.getId());
		activeUser.setUsercode(user.getLoginName());
		activeUser.setUsername(user.getName());
		//..
		
		//根据用户id取出菜单
		List<Permission> permissionList  = null;
		try {
			//通过service取出菜单 
			permissionList = permissionService.findMenuListByUserId(user.getId());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//将用户菜单 设置到activeUser
	
		List<String> permissions = new ArrayList<String>();
		if(permissionList.size()>0){
			for (int i = 0; i < permissionList.size(); i++) {
				permissions.add(permissionList.get(i).getResource());
			}
		}
		activeUser.setMenus(permissionList);
		activeUser.setPermissions(permissionList);
		this.setSession("USER", user);
		SimpleAuthenticationInfo simpleAuthenticationInfo =  new SimpleAuthenticationInfo(
				activeUser, password, this.getName());
		
		
		return simpleAuthenticationInfo;*/
	}

	// 用于授权
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
				return null;
		/*
		//从 principals获取主身份信息
		//将getPrimaryPrincipal方法返回值转为真实身份类型（在上边的doGetAuthenticationInfo认证通过填充到SimpleAuthenticationInfo中身份类型），
		ActiveUser activeUser =  (ActiveUser) principals.getPrimaryPrincipal();
		
		//根据身份信息获取权限信息
		//从数据库获取到权限数据
		List<Permission> permissionList = null;
		try {
			permissionList = permissionService.findPermissionListByUserId(activeUser.getUserid());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//单独定一个集合对象 
		List<String> permissions = new ArrayList<String>();
		if(permissionList.size()>0){
			for(int i=0; i<permissionList.size();i++){
				//将数据库中的权限标签 符放入集合
				permissions.add(permissionList.get(i).getPermissionCode());
			}
		}
		//查到权限数据，返回授权信息(要包括 上边的permissions)
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		//将上边查询到授权信息填充到simpleAuthorizationInfo对象中
		simpleAuthorizationInfo.addStringPermissions(permissions);
		
		
			return simpleAuthorizationInfo;
		*/
	}
	
	//清除缓存
	public void clearCached() {
		PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
		super.clearCache(principals);
	}

	/**
	 * 将一些数据放到ShiroSession中,以便于其它地方使用
	 * 
	 * @see 比如Controller,使用时直接用HttpSession.getAttribute(key)就可以取到
	 */
	private void setSession(Object key, Object value) {
		Subject currentUser = SecurityUtils.getSubject();
		if (null != currentUser) {
			Session session = currentUser.getSession();
			//System.out.println("AuthRealm.setSession():Session默认超时时间为[" + session.getTimeout() + "]毫秒");
			if (null != session) {
				session.setAttribute(key, value);
			}
		}
	}

}
