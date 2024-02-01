package com.example.vuespringjava.service.impl;

import com.example.vuespringjava.entity.SysUser;
import com.example.vuespringjava.entity.SysUserTeacher;
import com.example.vuespringjava.mapper.SysUserTeacherMapper;
import com.example.vuespringjava.service.SysUserService;
import com.example.vuespringjava.service.SysUserTeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 辞镜
 * @since 2022-08-16
 */
@Service
public class SysUserTeacherServiceImpl extends ServiceImpl<SysUserTeacherMapper, SysUserTeacher> implements SysUserTeacherService {
    @Autowired
    SysUserTeacherMapper sysUserTeacherMapper;
    @Autowired
    SysUserService sysUserService;
    @Override
    public SysUserTeacher getByUsername(String name) {
        SysUser username = sysUserService.getByUsername(name);

            SysUserTeacher sysUserTeacher= sysUserTeacherMapper.getTeacher(username.getId());

            return sysUserTeacher;
    }
}
