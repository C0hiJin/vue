package com.example.vuespringjava.controller;

import com.example.vuespringjava.common.lang.Result;
import com.example.vuespringjava.entity.SysUser;
import com.example.vuespringjava.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;

@Slf4j
@RestController
public class TestController {

    @Autowired
    SysUserService sysUserService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test")
    public Result test() {
        return Result.succ(sysUserService.list());
    }

    @PreAuthorize("hasAnyRole('admin')")
    @GetMapping("/test/admin")
    public Result test2() {
        return Result.succ(sysUserService.list());
    }

    @GetMapping("/test/pass")
    public Result pass() {
        String encode = bCryptPasswordEncoder.encode("111111");
        boolean matches = bCryptPasswordEncoder.matches("111111", encode);
        log.info("匹配结果:" + matches);
        return Result.succ(encode);
    }

    //文件上传接口
    @PostMapping("/test/uplond")
    public Result uplond(MultipartHttpServletRequest multipartHttpServletRequest, Principal Principal) throws IOException {
        MultipartFile file = multipartHttpServletRequest.getFile("file");
//        先通过后缀判断文件格式是否满足.zip文件的要求
        String lastName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        if (!lastName.equals(".zip")) {
            return Result.fail("文件格式应该为.zip");
        }
        SysUser user = sysUserService.getByUsername(Principal.getName());
        //        通过id和username生成独一无二的文件名
        String fileName = user.getId() + user.getUsername() + ".zip";

        //拼接
        //        设置文件存储路径
        String filePath = "upload/" + fileName;

        File dest = new File(new File(filePath).getAbsolutePath());
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest);
        return Result.succ("上传成功");
    }

    //    文件下载接口
    @GetMapping("/test/download/{id}")
    public Result download(@PathVariable("id") Long id,HttpServletRequest request, HttpServletResponse response) throws IOException {
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

}
