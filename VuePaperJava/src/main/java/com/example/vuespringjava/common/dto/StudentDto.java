package com.example.vuespringjava.common.dto;

import lombok.Data;

@Data
public class StudentDto {
//    id":5,
//                        "name": "学生1",
//                        "email": "2441865181@qq.com",
//                        "statu": 0,
    private Long id;
    private String name;
    private String email;
    private Integer statu;
}
