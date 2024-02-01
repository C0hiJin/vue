package com.example.vuespringjava.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.vuespringjava.service.*;
import com.example.vuespringjava.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

public class BaseController {

    @Autowired
    HttpServletRequest request;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysMenuService sysMenuService;

    @Autowired
    SysRoleMenuService sysRoleMenuService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    public Page getPage(){
//        获取当前页码
        int current= ServletRequestUtils.getIntParameter(request,"current",1);
        int size= ServletRequestUtils.getIntParameter(request,"size",10);

        return new Page(current,size);

    }
}
