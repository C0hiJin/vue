package com.example.vuespringjava.controller;


import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.vuespringjava.common.dto.SysMenuDto;
import com.example.vuespringjava.common.lang.Result;
import com.example.vuespringjava.entity.SysMenu;
import com.example.vuespringjava.entity.SysRoleMenu;
import com.example.vuespringjava.entity.SysUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
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
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController {
    /**
     * 获取当前菜单和用户权限信息
     *
     * @param principal
     * @return
     */
    @GetMapping("/nav")
    public Result nav(Principal principal) {
        SysUser sysUser = sysUserService.getByUsername(principal.getName());

//        获取权限信息，因为权限信息是用，隔开的在返回到前端时应该做处理，将其转化问数组
        String userAuthorityInfo = sysUserService.getUserAuthorityInfo(sysUser.getId());
        String[] userAuthorityInfoS = StringUtils.tokenizeToStringArray(userAuthorityInfo, ",");
//        获取导航栏信息
        List<SysMenuDto> navs = sysMenuService.getCurrentserNav();
        return Result.succ(MapUtil.builder()
                .put("authoriztys", userAuthorityInfoS)
                .put("nav", navs)
                .map()
        );
    }

    // 获取info
    @PreAuthorize("hasAnyAuthority('sys:menu:list')")
    @GetMapping("/info/{id}")
    public Result info(@PathVariable(name = "id") Long id) {

        return Result.succ(sysMenuService.getById(id));
    }

    // 获取list
    @PreAuthorize("hasAnyAuthority('sys:menu:list')")
    @GetMapping("/list")
    public Result list() {
        List<SysMenu> Tree = sysMenuService.tree();
//        构建树状结构

        return Result.succ(Tree);
    }

    //
    @PreAuthorize("hasAnyAuthority('sys:menu:save')")
    @PostMapping("/save")
    public Result save(@Validated @RequestBody SysMenu sysMenu) {
//  设置创建时间
        sysMenu.setCreated(LocalDateTime.now());
//        保存到数据库中
        sysMenuService.save(sysMenu);
        return Result.succ(sysMenu);
    }

    //
    @PreAuthorize("hasAnyAuthority('sys:menu:update')")
    @PostMapping("/update")
    public Result Update(@Validated @RequestBody SysMenu sysMenu) {
//设置修改时间
        sysMenu.setUpdated(LocalDateTime.now());
//        修改数据库，修改了信息需要更新缓冲
        sysMenuService.updateById(sysMenu);
//        清除缓冲
        sysUserService.clearUserAuthorityInfoByMenuID(sysMenu.getId());

        return Result.succ(sysMenu);
    }
    @PreAuthorize("hasAnyAuthority('sys:menu:delete')")
    @PostMapping("/delete/{id}")
    public Result delete(@PathVariable(name = "id") Long id) {
//        先判断是否由子集,若有子集则会因为SQL问题报错
//        通过parent_id判断是否由子集
        int count = sysMenuService.count(new QueryWrapper<SysMenu>().eq("parent_id", id));
        if (count >0){
            return Result.fail("请先删除子菜单");
        }
//        清除相关的缓存
        sysUserService.clearUserAuthorityInfoByMenuID(id);
        sysMenuService.removeById(id);
//        删除当前关联表
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenu>().eq(
                "menu_id", id
        ));
        return Result.succ("操作成功");
    }
}
