package com.example.vuespringjava.controller;

import cn.hutool.core.map.MapUtil;
import com.example.vuespringjava.common.dto.PaperDto;
import com.example.vuespringjava.common.dto.StudentDto;
import com.example.vuespringjava.common.lang.Result;
import com.example.vuespringjava.entity.SysPaper;
import com.example.vuespringjava.entity.SysUser;
import com.example.vuespringjava.entity.SysUserTeacher;
import com.example.vuespringjava.mapper.SysPaperMapper;
import com.example.vuespringjava.service.SysPaperService;
import com.example.vuespringjava.service.SysUserService;
import com.example.vuespringjava.service.SysUserTeacherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/sys/teacher")
public class SysTeacherController {
    @Autowired
    SysUserTeacherService sysUserTeacherService;
    @Autowired
    SysPaperService sysPaperService;
    @Autowired
    SysPaperMapper sysPaperMapper;
    @Autowired
    SysUserService sysUserService;
    @GetMapping("/list")
    public Result getTeacherList(Principal principal){
        List<PaperDto> records=new ArrayList<>();
//        List<StudentDto> studentDtos=new ArrayList<>();
//        先获取教师,即先用户的教师ID
        log.info(principal.getName());
        SysUserTeacher sysUserTeacher= sysUserTeacherService.getByUsername(principal.getName());

        Long id=sysUserTeacher.getTeacherId();

//       通过教师ID获取paper,再将paper包装成PaperDto
        List<SysPaper> paper = sysPaperMapper.getTeacherPaper(sysUserTeacher.getTeacherId());
//        通过paperID获取选取的学生
        paper.stream().forEach(p->{
            List<StudentDto> users=new ArrayList<>();
//            为下属学生表注入内容
            Long PAPERID=p.getId();
            List<SysUser> allStudent = sysPaperMapper.getALLStudent(PAPERID);
            allStudent.stream().forEach(u->{
                StudentDto dto = new StudentDto();
                dto.setId(u.getId());
                dto.setEmail(u.getEmail());
                dto.setName(u.getUsername());
                dto.setStatu(u.getStatu());
                log.info("xxxx");
                users.add(dto);
            });
            PaperDto paperDto = new PaperDto();
            paperDto.setId(p.getId());
            paperDto.setName(p.getName());
            SysUser teacher=sysPaperMapper.getTeacher(p.getId());
            paperDto.setTeacherName(sysPaperMapper.getTeacher(p.getId()).getUsername());
            paperDto.setRemark(p.getRemark());
            paperDto.setChildren(users);
//            完成paperDto的封装
            records.add(paperDto);
        });
        return Result.succ(
                MapUtil.builder()
                        .put("records",records)
                        .map()

        );
    }
    @GetMapping("/download/{id}")
    public Result download(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        通过id获取要下载的论文的用户信息
        SysUser user = sysUserService.getById(id);
        String fileName = user.getId() + user.getUsername() + ".zip";

//        文件保存的路径
        String filePath = "upload/" + fileName;
        log.info(filePath);
        download(request,response,filePath,user.getUsername()+".zip");
        return Result.succ("");
    }
    void download(HttpServletRequest request, HttpServletResponse response, String downloadPath,String fileName) throws IOException {

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Content-Length", String.valueOf(new File(downloadPath).length()));
        request.setCharacterEncoding("UTF-8");

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(downloadPath));
        BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
        byte[] buff = new byte[2048];
        int bytesRead;
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            bos.write(buff, 0, bytesRead);
        }
        bis.close();
        bos.close();
    }
   @PostMapping("/add")
    public Result add(@Validated @RequestBody SysPaper sysPaper,Principal Principal){
        log.info(sysPaper.getName());


//
        return Result.succ("");
    }

}
