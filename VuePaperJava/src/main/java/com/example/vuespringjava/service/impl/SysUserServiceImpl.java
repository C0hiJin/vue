package com.example.vuespringjava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vuespringjava.entity.SysMenu;
import com.example.vuespringjava.entity.SysRole;
import com.example.vuespringjava.entity.SysUser;
import com.example.vuespringjava.mapper.SysUserMapper;
import com.example.vuespringjava.service.SysMenuService;
import com.example.vuespringjava.service.SysRoleService;
import com.example.vuespringjava.service.SysUserService;
import com.example.vuespringjava.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 辞镜
 * @since 2022-07-18
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    SysMenuService menuService;

    @Autowired
    RedisUtil redisUtil;

    //    因为权限等信会被多次调用若每一次调用都使用数据库进行获取的话,会造成系统效率下降
//    所以使用redis缓存权限信息,只需要获取一次即可
    @Override
    public SysUser getByUsername(String username) {
//        通过唯一标识获取实体
        return getOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    @Override
    public String getUserAuthorityInfo(Long userID) {
        SysUser sysUsers = sysUserMapper.selectById(userID);
//        通过id获取权限信息和角色信息(格式:ROLE_admin)
        String authority = "";

        if (redisUtil.hasKey("GrantedAuthority:" + sysUsers.getUsername())) {

            authority = (String) redisUtil.get("GrantedAuthority:" + sysUsers.getUsername());

        } else {
            //    获取id
            List<SysRole> sysRoles = sysRoleService.list(new QueryWrapper<SysRole>().inSql("id", "select role_id from sys_user_role where user_id= " + userID));
//获取角色权限
            if (sysRoles.size() > 0) {
//            直接获取所有编码,通过，隔开 获取
                String roleCodes = sysRoles.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
                authority = roleCodes.concat(",");
            }
//        获取菜单操作权限
//        1.获取id
            List<Long> menuIds = sysUserMapper.getNavMenuIds(userID);
            if (menuIds.size() > 0) {
//            2.通过id获取对应的操作编码
                List<SysMenu> sysMenuList = menuService.listByIds(menuIds);
                String menuPerms = sysMenuList.stream().map(m -> m.getPerms()).collect(Collectors.joining(","));
//            3.将菜单操作权限加入到authority
                authority = authority.concat(menuPerms);
            }
            redisUtil.set("GrantedAuthority:" + sysUsers.getUsername(), authority, 60 * 60 * 24);

        }

        return authority;
    }
// 当用户信息改变时删除权限信息
    @Override
    public void clearUserAuthorityInfo(String userName) {
    redisUtil.del("GrantedAuthority:" + userName);
    }
// 当角色信息改变时删除权限
    @Override
    public void clearUserAuthorityInfoByRoleID(Long RoleID) {
        List<SysUser> sysUsers = this.list(new QueryWrapper<SysUser>().inSql("id",
                "select user_id from sys_user_role where role_id=" + RoleID));
        sysUsers.forEach(u->{
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }
//当菜单信息改变时删除权限信息
    @Override
    public void clearUserAuthorityInfoByMenuID(Long MenuID) {
        List<SysUser> sysUsers=sysUserMapper.listByMenuID(MenuID);
        sysUsers.forEach(u->{
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }
}
