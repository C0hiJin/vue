package com.example.vuespringjava.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  {
 *                     title: '菜单管理',
 *                     name: 'SysManga',
 *                     icon: 'el-icon-menu',
 *                     path: '/sys/menu',
 *                     children: [],
 *                     component:'sys/Menu'
 *                 },
 */
@Data
public class SysMenuDto  implements Serializable {

    private Long id;
    private String title;
    private String name;
    private String icon;
    private String path;
    private List<SysMenuDto> children=new ArrayList<>();
    private String component;
}
