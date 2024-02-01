package com.example.vuespringjava.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.vuespringjava.entity.SysPaper;
import com.example.vuespringjava.entity.SysUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 辞镜
 * @since 2022-08-16
 */
@Repository
public interface SysPaperMapper extends BaseMapper<SysPaper> {
    List<SysPaper> getTeacherPaper(Long TeacherID);
    List<SysUser> getALLStudent(Long paperID);
    SysUser  getTeacher(Long paperID);
    SysPaper getPaper(Long userID);
    List<SysPaper> getAll();
    void   StudentCourseSelection(Long userID,Long paperID);
    void  addPaper(String name,Long TeacherID,String remark);

}
