package com.example.vuespringjava.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 辞镜
 * @since 2022-08-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserStuden extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long paperId;


}
