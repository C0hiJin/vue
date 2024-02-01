package com.example.vuespringjava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vuespringjava.common.dto.SysMenuDto;
import com.example.vuespringjava.entity.SysMenu;
import com.example.vuespringjava.entity.SysUser;
import com.example.vuespringjava.mapper.SysMenuMapper;
import com.example.vuespringjava.mapper.SysUserMapper;
import com.example.vuespringjava.service.SysMenuService;
import com.example.vuespringjava.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 辞镜
 * @since 2022-07-18
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysUserMapper sysUserMapper;

    @Override
    public List<SysMenuDto> getCurrentserNav() {
//        因为并没有选择通过参数传递用户信息,但用户已经登录所以通过security获取上下文获取用户信息
        String UserName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysUser sysUser = sysUserService.getByUsername(UserName);
//        获取菜单信息id
        List<Long> menuIds = sysUserMapper.getNavMenuIds(sysUser.getId());
//        通过菜单ID获取对应的菜单信息
        List<SysMenu> sysMenus = this.listByIds(menuIds);
//        转树状结构
        List<SysMenu> menuTree = buildTreeMenu(sysMenus);
//        实体转DTO
        return convet(menuTree);
    }

    @Override
    public List<SysMenu> tree() {
//        获取所有菜单信息
        List<SysMenu> sysMenus = this.list(new QueryWrapper<SysMenu>().orderByAsc("orderNum"));
        return buildTreeMenu(sysMenus);
//        转成树状结构

    }

    private List<SysMenu> buildTreeMenu(List<SysMenu> sysMenus) {
        List<SysMenu> finalMenus = new ArrayList<>();
//        先各自寻找各自的孩子
        for (SysMenu sysMenu : sysMenus) {
            for (SysMenu e : sysMenus) {
                if (sysMenu.getId() == e.getParentId()) {
                    sysMenu.getChildren().add(e);
                }
            }
            //        提取出父节点
            if (sysMenu.getParentId() == 0) {
                finalMenus.add(sysMenu);
            }
        }

        return finalMenus;
    }
    private List<SysMenuDto> convet(List<SysMenu> menuTree){
        ArrayList<SysMenuDto> sysMenuDtos = new ArrayList<>();
        menuTree.forEach(m->{
            SysMenuDto dto=new SysMenuDto();
            dto.setId(m.getId());
            dto.setName(m.getPerms());
            dto.setComponent(m.getComponent());
            dto.setIcon(m.getIcon());
            dto.setPath(m.getPath());
            dto.setTitle(m.getName());
           if(m.getChildren().size()>0){
//               子节点调用当前方法进行转换
               dto.setChildren(convet(m.getChildren()));
           }
            sysMenuDtos.add(dto);
        });

        return sysMenuDtos;
    }
}
