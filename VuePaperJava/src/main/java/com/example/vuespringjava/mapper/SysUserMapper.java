package com.example.vuespringjava.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.vuespringjava.entity.SysUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 辞镜
 * @since 2022-07-18
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {
    List<Long>getNavMenuIds(Long userID);
    List<SysUser> listByMenuID(Long MenuID);
}
