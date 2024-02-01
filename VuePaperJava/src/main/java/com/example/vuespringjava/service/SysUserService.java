package com.example.vuespringjava.service;

import com.example.vuespringjava.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 辞镜
 * @since 2022-07-18
 */
public interface SysUserService extends IService<SysUser> {

    SysUser getByUsername(String userName);
    String getUserAuthorityInfo(Long userID);
    void clearUserAuthorityInfo(String userName);
    void clearUserAuthorityInfoByRoleID(Long RoleID);
    void clearUserAuthorityInfoByMenuID(Long MenuID);


}
