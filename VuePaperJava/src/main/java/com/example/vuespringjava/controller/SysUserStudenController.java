package com.example.vuespringjava.controller;


import cn.hutool.core.map.MapUtil;
import com.example.vuespringjava.common.dto.PaperDto;
import com.example.vuespringjava.common.lang.Result;
import com.example.vuespringjava.entity.SysPaper;
import com.example.vuespringjava.entity.SysUser;
import com.example.vuespringjava.mapper.SysPaperMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.security.Principal;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 辞镜
 * @since 2022-08-16
 */
@RestController
@RequestMapping("/sys/student")
public class SysUserStudenController extends BaseController {
    @Autowired
    SysPaperMapper sysPaperMapper;
    @GetMapping("/list")
    public Result getStudenList(Principal Principal){
        SysUser username = sysUserService.getByUsername(Principal.getName());
//        通过用户ID获取选取的论文内容
        SysPaper paper = sysPaperMapper.getPaper(username.getId());
        PaperDto records = new PaperDto();
        records.setName(paper.getName());
        records.setRemark(paper.getRemark());
        records.setTeacherName(sysPaperMapper.getTeacher(paper.getId()).getUsername());
        records.setTeacherEmaiel(sysPaperMapper.getTeacher(paper.getId()).getEmail());
        records.setId(paper.getId());
        return Result.succ(MapUtil
                .builder()
                .put("records",records)
                        .put("statu",username.getStatu())
                .map()
        );
    }
    @PostMapping("/uplond")
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
//        成功修改状态
        user.setStatu(1);
        sysUserService.updateById(user);
        return Result.succ("上传成功");
    }

}
