package com.example.vuespringjava.service;

import com.example.vuespringjava.entity.SysUserTeacher;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 辞镜
 * @since 2022-08-16
 */
public interface SysUserTeacherService extends IService<SysUserTeacher> {
    SysUserTeacher  getByUsername(String name);
}
