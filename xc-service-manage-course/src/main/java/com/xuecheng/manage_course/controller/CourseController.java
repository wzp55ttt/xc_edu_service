package com.xuecheng.manage_course.controller;

import com.github.pagehelper.Page;
import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CmsPostPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Teachplan)表控制层
 *
 * @author makejava
 * @since 2020-03-22 12:18:34
 */
@RestController
@RequestMapping("course")
public class CourseController implements CourseControllerApi {
    /**
     * 服务对象
     */
    @Resource
    private CourseService courseService;

    /**
     * 查询课程计划
     * @param courseId
     * @return
     */
    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        return this.courseService.findTeachplanById(courseId);
    }

    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return this.courseService.addTeachplan(teachplan);
    }

    @Override
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult findCourses(@PathVariable("page") Integer page,
                                           @PathVariable("size") Integer size,
                                           CourseListRequest courseListRequest) {
        return this.courseService.findCourses(page,size);
    }

    @Override
    @GetMapping("findCourse/{id}")
    public CourseBase findCourseBase(@PathVariable("id") String courseId) {
        return this.courseService.findCourseId(courseId);
    }

    @Override
    @PutMapping("update/{id}")
    public ResponseResult update(@PathVariable("id") String courseId, @RequestBody CourseBase courseBase) {
        return this.courseService.update(courseId,courseBase);
    }

    @Override
    @PostMapping("coursebase/add")
    public ResponseResult add(@RequestBody CourseBase courseBase) {
        return this.courseService.addCourseBase(courseBase);
    }

    @Override
    @PostMapping("coursepic/add")
    public ResponseResult savePic(String courseId, String pic) {
        return this.courseService.savePic(courseId,pic);
    }

    @Override
    @GetMapping("coursepic/list/{courseId}")
    public CoursePic getPic(@PathVariable("courseId") String courseId) {
        return this.courseService.getPic(courseId);
    }

    @Override
    @DeleteMapping("coursepic/delete")
    public ResponseResult deleteCoursePic(@RequestParam("courseId") String courseId) {
        return this.courseService.deleteCoursePic(courseId);
    }

    @Override
    @GetMapping("courseview/{id}")
    public CourseView findCourseView(@PathVariable("id") String courseId) {
        return this.courseService.findCourseView(courseId);
    }

    @Override
    @PostMapping("/preview/{id}")
    public CoursePublishResult preview(@PathVariable("id") String courseId) {
        return this.courseService.preview(courseId);
    }

    @Override
    @PostMapping("/publish/{id}")
    public CoursePublishResult publish(@PathVariable("id") String courseId) {
        return this.courseService.publish(courseId);
    }
}