package com.example.vuespringjava.controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.example.vuespringjava.common.dto.PaperDto;
import com.example.vuespringjava.common.lang.Result;
import com.example.vuespringjava.entity.SysPaper;
import com.example.vuespringjava.entity.SysUser;
import com.example.vuespringjava.mapper.SysPaperMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sys/registration")
public class RegistrationController   extends BaseController{
    @Autowired
    SysPaperMapper sysPaperMapper;

    @GetMapping("/list")
    public Result GetList(){
//        {
//                id: 10,
//                "name": "one老师的第一篇",
//                "teacherName": "1老师",
//                "remark": "one老师发布的第一篇论文",
//                "email": "2441865181@qq.com",
//            },
        List<SysPaper> paperMapperAll = sysPaperMapper.getAll();
        List<PaperDto> records=new ArrayList<>();

        paperMapperAll.stream().forEach(p->{
            PaperDto paperDto = new PaperDto();
            SysUser teacher = sysPaperMapper.getTeacher(p.getId());
            paperDto.setName(p.getName());
            paperDto.setId(p.getId());
            paperDto.setTeacherEmaiel(teacher.getEmail());
            paperDto.setTeacherName(teacher.getUsername());
            paperDto.setRemark(p.getRemark());
            records.add(paperDto);
        });
        return  Result.succ(
                MapUtil.builder()
                        .put("records", records)
                        .map());
    }
    @PostMapping("/pitch/{id}")
    public Result pitch(@PathVariable("id") Long PaperId, Principal principal){

        SysUser pitchUsername = sysUserService.getByUsername(principal.getName());
//        该学生未选课
        SysPaper paper = sysPaperMapper.getPaper(pitchUsername.getId());

        if (StrUtil.isNotBlank(paper.getName())){
            return Result.fail("已选论文不可再次选取");
//
        }
        sysPaperMapper.StudentCourseSelection(pitchUsername.getId(),PaperId);
        return Result.succ("选取成功");
    }

}
