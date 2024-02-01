package com.example.vuespringjava.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author 辞镜
 * @since 2022-07-18
 */
@Data
public class SysUserRole {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long roleId;


}
