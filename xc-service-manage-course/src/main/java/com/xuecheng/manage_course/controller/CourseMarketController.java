package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseMarketControllerApi;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseMarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author gakki
 */
@RestController
@RequestMapping("course/market")
public class CourseMarketController implements CourseMarketControllerApi {

    @Autowired
    CourseMarketService courseMarketService;

    @Override
    @GetMapping("{course}")
    public CourseMarket getCourseMarcketById(@PathVariable("course") String courseId) {
        return this.courseMarketService.findCourseMarkById(courseId);
    }

    @Override
    @PutMapping("{course}")
    public ResponseResult update(@PathVariable("course") String courseId,@RequestBody CourseMarket courseMarket) {
        return this.courseMarketService.update(courseId,courseMarket);
    }

}
