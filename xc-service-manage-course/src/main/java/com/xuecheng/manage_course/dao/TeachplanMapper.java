package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author gakki
 */
@Mapper
public interface TeachplanMapper {
    //课程计划查询的方法
    public TeachplanNode selectList(String courseId);
}
