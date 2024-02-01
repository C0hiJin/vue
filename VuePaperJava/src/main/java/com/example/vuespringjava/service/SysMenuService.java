package com.example.vuespringjava.service;

import com.example.vuespringjava.common.dto.SysMenuDto;
import com.example.vuespringjava.entity.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 辞镜
 * @since 2022-07-18
 */
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenuDto> getCurrentserNav();

    List<SysMenu> tree();
}
