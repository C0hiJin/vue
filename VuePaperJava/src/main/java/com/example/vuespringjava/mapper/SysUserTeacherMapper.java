package com.example.vuespringjava.mapper;

import com.example.vuespringjava.entity.SysUserTeacher;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 辞镜
 * @since 2022-08-16
 */
@Repository
public interface SysUserTeacherMapper extends BaseMapper<SysUserTeacher> {
    SysUserTeacher getTeacher(Long userID);
}
