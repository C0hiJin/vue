package com.example.vuespringjava.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.vuespringjava.common.dto.PassWordDto;
import com.example.vuespringjava.common.lang.Const;
import com.example.vuespringjava.common.lang.Result;
import com.example.vuespringjava.entity.SysRole;
import com.example.vuespringjava.entity.SysUser;
import com.example.vuespringjava.entity.SysUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 辞镜
 * @since 2022-07-18
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/INFO")
    public Result INFO(Principal principal){
        return Result.succ(principal.getName());
    }
    @PreAuthorize("hasAnyAuthority('sys:user:list')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id) {

        SysUser user = sysUserService.getById(id);
        Assert.notNull(user,"找不到该管理员,请确认ID是否正确");

      List<SysRole> roles= sysRoleService.listRolesByUserID(id);
        user.setSysRoles(roles);
        return Result.succ(user);
    }
    @PreAuthorize("hasAnyAuthority('sys:user:list')")
    @GetMapping("/list")
    public Result list( String username) {
        Page<SysUser> page = sysUserService.page(getPage(), new QueryWrapper<SysUser>()
                .like(StrUtil.isNotBlank(username), "username", username)
        );
        page.getRecords().forEach(u->{
            u.setSysRoles(sysRoleService.listRolesByUserID(u.getId()));
        });

        return Result.succ(page);
    }
    @PreAuthorize("hasAnyAuthority('sys:user:save')")
    @PostMapping("/save")
    public Result save(@Validated @RequestBody SysUser sysUser) {
        sysUser.setCreated(LocalDateTime.now());
        sysUser.setStatu(Const.Status_ON);
//        重置初始密码操作 为111111
        String encode = passwordEncoder.encode(Const.pass_Word);
        sysUser.setPassword(encode);
//        设置默认头像
        sysUser.setAvatar(Const.DEF_AVATAR);
        sysUserService.save(sysUser);
        return Result.succ(sysUser);
    }
    @PreAuthorize("hasAnyAuthority('sys:user:update')")
    @PostMapping("/update")
    public Result update( @Validated @RequestBody SysUser sysUser) {
            sysUser.setUpdated(LocalDateTime.now());
            sysUserService.updateById(sysUser);
        return Result.succ(sysUser);
    }

    @Transactional
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('sys:user:delete')")
    public Result delete(@RequestBody Long[] ids) {

        sysUserService.removeByIds(Arrays.asList(ids));
        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id", ids));

        return Result.succ("");
    }
    @Transactional
    @PostMapping("/role/{userId}")
    @PreAuthorize("hasAuthority('sys:user:role')")
    public Result rolePerm(@PathVariable("userId") Long userId, @RequestBody Long[] roleIds) {

        List<SysUserRole> userRoles = new ArrayList<>();

        Arrays.stream(roleIds).forEach(r -> {
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(r);
            sysUserRole.setUserId(userId);

            userRoles.add(sysUserRole);
        });

        sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        sysUserRoleService.saveBatch(userRoles);

        // 删除缓存
        SysUser sysUser = sysUserService.getById(userId);
        sysUserService.clearUserAuthorityInfo(sysUser.getUsername());

        return Result.succ("");
    }
    @PreAuthorize("hasAnyAuthority('sys:user:repass')")
    @PostMapping("/repass")
    public Result repass(@RequestBody Long userID) {
        SysUser serviceById = sysUserService.getById(userID);
        serviceById.setPassword(passwordEncoder.encode(Const.pass_Word));
        serviceById.setUpdated(LocalDateTime.now());
        sysUserService.updateById(serviceById);
        return Result.succ("");
    }

    @PostMapping("/updatePass")
    public Result updatePassWord(@Validated @RequestBody PassWordDto passWordDto, Principal principal) {

        SysUser username = sysUserService.getByUsername(principal.getName());
        boolean matches = passwordEncoder.matches(passWordDto.getCurrentPass(), username.getPassword());
        if (!matches){
            return  Result.fail("旧密码匹配不正确");
        }
        username.setPassword(passwordEncoder.encode(passWordDto.getPassword()));
        username.setUpdated(LocalDateTime.now());
        sysUserService.updateById(username);
        return Result.succ("");
    }
}
