package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author gakki
 */
@Api(value = "课程营销")
public interface CourseMarketControllerApi {

    @ApiOperation("获取课程营销")
    public CourseMarket getCourseMarcketById(String courseId);

    @ApiOperation("更新课程营销")
    public ResponseResult update(String courseId, CourseMarket courseMarket);
}
