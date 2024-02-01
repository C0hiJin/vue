package com.example.vuespringjava.common.dto;


import lombok.Data;

import java.util.List;

//id: 10,
//                "name": "one老师的第一篇",
//                "teacherName": "1老师",
//                "remark": "one老师发布的第一篇论文",
//                "email": "2441865181@qq.com",
//                  children:
@Data
public class PaperDto {
    private Long id;
    private String name;
    private String teacherName;
    private String remark;
    private String TeacherEmaiel;
    private List<StudentDto> children;

}
