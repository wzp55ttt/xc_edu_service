package com.xuecheng.api.course;

import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author gakki
 */
@Api(value = "课程管理接口",description = "课程管理接口，提供课程的增删改查")
public interface CourseControllerApi {

    @ApiOperation(("课程计划查询"))
    public TeachplanNode findTeachplanList(String courseId);

    @ApiOperation(("添加课程计划"))
    public ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation(("查询所有课程"))
    public QueryResponseResult findCourses(Integer page, Integer size, CourseListRequest courseListRequest);

    @ApiOperation(("根据id查询课程信息"))
    public CourseBase findCourseBase(String courseId);

    @ApiOperation(("更新课程信息"))
    public ResponseResult update(String courseId, CourseBase courseBase);

    @ApiOperation(("添加课程"))
    public ResponseResult add(CourseBase courseBase);

    @ApiOperation(("保存图片"))
    public ResponseResult savePic(String courseId, String pic);

    @ApiOperation(("获取图片"))
    public CoursePic getPic(String courseId);

    @ApiOperation(("删除图片"))
    public ResponseResult deleteCoursePic(String courseId);

    @ApiOperation(("查询课程视图信息"))
    public CourseView findCourseView(String courseId);

    @ApiOperation(("页面预览"))
    public CoursePublishResult preview(String courseId);

    @ApiOperation("发布课程")
    public CoursePublishResult publish(String courseId);


}
