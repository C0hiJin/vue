package com.example.vuespringjava.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data

public class SysPaper extends BaseEntity {
    @NotBlank(message = "论文名不可为空")
    private String name;

    private String teacherId;
    @NotBlank(message = "论文描述不可为空")
    private String remark;
    @TableField(exist = false)
    private String teacherName;
}
