package com.example.vuespringjava.service;

import com.example.vuespringjava.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 辞镜
 * @since 2022-07-18
 */
public interface SysRoleService extends IService<SysRole> {

    List<SysRole> listRolesByUserID(Long id);
}
