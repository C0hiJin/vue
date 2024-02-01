package com.example.vuespringjava.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.vuespringjava.entity.SysRole;
import com.example.vuespringjava.mapper.SysRoleMapper;
import com.example.vuespringjava.service.SysRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 辞镜
 * @since 2022-07-18
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Override
    public List<SysRole> listRolesByUserID(Long id) {
        List<SysRole> sysRoles = this.list(new QueryWrapper<SysRole>()
                .inSql("id", "SELECT role_id FROM sys_user_role WHERE user_id =" + id)
        );
        return sysRoles;
    }
}
