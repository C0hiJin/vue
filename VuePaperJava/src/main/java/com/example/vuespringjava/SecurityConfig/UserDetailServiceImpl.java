package com.example.vuespringjava.SecurityConfig;

import com.example.vuespringjava.entity.SysUser;
import com.example.vuespringjava.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
//
    @Autowired
    SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUserServiceByUsername = sysUserService.getByUsername(username);

        if(sysUserServiceByUsername==null){
            throw  new UsernameNotFoundException("用户名或者密码错误");
        }

        return new AccountUser(sysUserServiceByUsername.getId(),
                sysUserServiceByUsername.getUsername(),
                sysUserServiceByUsername.getPassword(),
                getUserAuthority(sysUserServiceByUsername.getId())
                );
    }
//    通过用户id获取权限信息
    public List<GrantedAuthority>getUserAuthority(long userid){
//获取角色、菜单操作权限
        String userAuthorityInfo = sysUserService.getUserAuthorityInfo(userid);

        return AuthorityUtils.commaSeparatedStringToAuthorityList(userAuthorityInfo);
    }
}
