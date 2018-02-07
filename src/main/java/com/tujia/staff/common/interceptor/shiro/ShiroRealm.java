package com.tujia.staff.common.interceptor.shiro;


import com.tujia.staff.common.utils.Const;
import com.tujia.staff.model.user.Permission;
import com.tujia.staff.model.user.Role;
import com.tujia.staff.model.user.User;
import com.tujia.staff.service.user.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by haibingm on 2018/1/11.
 */
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;
    /*
	 * 登录信息和用户验证信息验证(non-Javadoc)
	 * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
	 */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)token;
        String username = usernamePasswordToken.getUsername();
        //验证是否存在该user
        User user = userService.findUserByUserName(username);
        if(user == null){
            return null;
        }else {
            // 设置session
            Session session = SecurityUtils.getSubject().getSession();
            session.setAttribute(Const.SESSION_USER, user);
            Object principal = token.getPrincipal();
            return  new SimpleAuthenticationInfo(principal,user.getPassword(),this.getName());
        }
    }

    /*
     * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用,负责在应用程序中决定用户的访问控制的方法(non-Javadoc)
     * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection pc) {

        String username = (String)pc.getPrimaryPrincipal();
        User user = userService.findUserByUserName(username);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        for (Role role : user.getRoleList()){
            authorizationInfo.addRole(role.getRole());
            for(Permission permission : role.getPermissionList()){
                authorizationInfo.addStringPermission(permission.getPermission());
            }
        }
        return authorizationInfo;
    }
}
