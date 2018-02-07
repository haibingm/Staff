package com.tujia.staff.common.security;

import com.tujia.staff.common.utils.SecurityUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by haibingm on 2018/1/16.
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher{
    @Override
    public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo){

        try {
            UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken)authenticationToken;
            String password = String.valueOf(usernamePasswordToken.getPassword());
            Object tokenCredentials = SecurityUtil.encryptPassword(password);
            Object accountCredentials = getCredentials(authenticationInfo);
            return equals(tokenCredentials,accountCredentials);
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return false;
    }
}
