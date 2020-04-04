package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmspageClient;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.monitor.CounterMonitor;
import java.util.List;
import java.util.Optional;

/**
 * @author gakki
 */
@Service
public class CourseService {

    @Autowired
    private TeachplanMapper teachplanMapper;
    @Autowired
    private CourseBaseRepository courseBaseRepository;
    @Autowired
    private TeachplanRepository teachplanRepository;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private CoursePicRepository coursePicRepository;
    @Autowired
    private CourseMarketRepository courseMarketRepository;
    @Autowired
    private CmspageClient cmspageClient;

    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;

    /**
     * 根据id查询teachplan
     *
     * @param teachplanId
     * @return
     */
    public TeachplanNode findTeachplanById(String teachplanId) {
        return this.teachplanMapper.selectList(teachplanId);
    }

    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
        if (teachplan == null || StringUtils.isEmpty(teachplan.getPname()) || StringUtils.isEmpty(teachplan.getCourseid())) {
            return null;
        }
        String courseid = teachplan.getCourseid();
        String parentId = teachplan.getParentid();
        //如果id为空，则查询课程
        if (StringUtils.isEmpty(parentId)) {
            parentId = findTeachplanId(courseid);
            teachplan.setParentid(parentId);
        }
        Optional<Teachplan> parent = this.teachplanRepository.findById(parentId);
        Teachplan parentTeach = parent.get();
        String grade = parentTeach.getGrade();
        if ("1".equals(grade)) {
            teachplan.setGrade("2");
        } else {
            teachplan.setGrade("3");
        }
        //新节点
        Teachplan teachplanNew = new Teachplan();
        //将页面提交的teachplan信息拷贝到teachplanNew对象中
        BeanUtils.copyProperties(teachplan, teachplanNew);
        this.teachplanRepository.save(teachplanNew);

        return new ResponseResult(CommonCode.SUCCESS);
    }

    private String findTeachplanId(String courseId) {
        //查询有没有这个课程
        Optional<CourseBase> byId = this.courseBaseRepository.findById(courseId);
        if (!byId.isPresent()) {
            //没有，抛异常
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        CourseBase courseBase = byId.get();
        //查询是否有课程计划，没有就增加一个
        List<Teachplan> teachplans = this.teachplanRepository.findByCourseidAndParentid(courseId, "0");
        if (teachplans == null || teachplans.size() == 0) {
            //保存
            Teachplan teachplan = new Teachplan();
            teachplan.setParentid("0");
            teachplan.setPname(courseBase.getName());
            teachplan.setGrade("1");
            teachplan.setCourseid(courseId);
            teachplan.setStatus("0");
            this.teachplanRepository.save(teachplan);
            return teachplan.getId();
        }

        return teachplans.get(0).getId();
    }

    public QueryResponseResult findCourses(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        Page<CourseList> courses = this.courseMapper.findCourses();
        //封装查询结果集
        QueryResult<CourseList> res = new QueryResult<>();
        res.setList(courses.getResult());
        res.setTotal(courses.getTotal());
        return new QueryResponseResult(CommonCode.SUCCESS, res);
    }

    public CourseBase findCourseId(String courseId) {
        Optional<CourseBase> byId = this.courseBaseRepository.findById(courseId);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }

    @Transactional
    public ResponseResult update(String courseId, CourseBase courseBase) {
        //判断是否存在
        Optional<CourseBase> course = this.courseBaseRepository.findById(courseId);
        if (!course.isPresent()) {
            return new ResponseResult(CommonCode.FAIL);
        }
        CourseBase courseBase1 = course.get();
        courseBase1.setName(courseBase.getName());
        courseBase1.setUsers(courseBase.getUsers());
        courseBase1.setMt(courseBase.getMt());
        courseBase1.setSt(courseBase.getSt());
        courseBase1.setGrade(courseBase.getGrade());
        courseBase1.setStudymodel(courseBase.getStudymodel());
        CourseBase save = this.courseBaseRepository.saveAndFlush(courseBase1);
        if (save != null) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    @Transactional
    public ResponseResult addCourseBase(CourseBase courseBase) {
        CourseBase save = this.courseBaseRepository.save(courseBase);
        if (save != null) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    @Transactional
    public ResponseResult savePic(String courseId, String pic) {
        Optional<CoursePic> res = this.coursePicRepository.findById(courseId);
        CoursePic coursePic = null;
        if (res.isPresent()) {
            coursePic = res.get();
        } else {
            coursePic = new CoursePic();
            coursePic.setCourseid(courseId);
        }
        coursePic.setPic(pic);
        CoursePic save = this.coursePicRepository.save(coursePic);
        if (save == null) {
            return new ResponseResult(CommonCode.FAIL);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CoursePic getPic(String courseId) {
        Optional<CoursePic> byId = this.coursePicRepository.findById(courseId);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }

    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {
        Long res = this.coursePicRepository.deleteByCourseid(courseId);
        if (res > 0) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    public CourseView findCourseView(String courseId) {
        CourseView courseView = new CourseView();
        //查询课程基本信息
        Optional<CourseBase> courseBaseOptional = this.courseBaseRepository.findById(courseId);
        if (courseBaseOptional.isPresent()) {
            courseView.setCourseBase(courseBaseOptional.get());
        }
        //查询课程图片信息
        Optional<CoursePic> coursePicOptional = this.coursePicRepository.findById(courseId);
        if (coursePicOptional.isPresent()) {
            courseView.setCoursePic(coursePicOptional.get());
        }
        //查询课程营销
        Optional<CourseMarket> courseMarketOptional = this.courseMarketRepository.findById(courseId);
        if (courseMarketOptional.isPresent()) {
            courseView.setCourseMarket(courseMarketOptional.get());
        }
        //课程计划
        TeachplanNode teachplanNode = this.teachplanMapper.selectList(courseId);
        courseView.setTeachplanNode(teachplanNode);
        return courseView;
    }

    /**
     * 课程视图预览
     * @param courseId
     * @return
     */
    public CoursePublishResult preview(String courseId) {
        //根据课程id查询课程
        Optional<CourseBase> courseBaseOptional = this.courseBaseRepository.findById(courseId);
        if (!courseBaseOptional.isPresent()) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        CourseBase courseBase = courseBaseOptional.get();
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setPageName(courseId + ".html");
        cmsPage.setPageAliase(courseBase.getName());
        cmsPage.setDataUrl(publish_dataUrlPre + courseId);
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setTemplateId(publish_templateId);
        //保存
        CmsPageResult pageResult = this.cmspageClient.save(cmsPage);
        if (!pageResult.isSuccess()) {
            //保存失败
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        //保存成功，返回url
        String url = previewUrl + pageResult.getCmsPage().getPageId();
        return new CoursePublishResult(CommonCode.SUCCESS, url);
    }

    /**
     * 课程发布
     * @param courseId
     * @return
     */
    @Transactional
    public CoursePublishResult publish(String courseId) {
        //根据课程id查询课程
        Optional<CourseBase> courseBaseOptional = this.courseBaseRepository.findById(courseId);
        if (!courseBaseOptional.isPresent()) {
            return new CoursePublishResult(CommonCode.FAIL, null);
        }
        CourseBase courseBase = courseBaseOptional.get();
        //课程信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setDataUrl(publish_dataUrlPre + courseId);
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setPageName(courseId + ".html");
        cmsPage.setPageAliase(courseBase.getName());
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setTemplateId(publish_templateId);
        //发布
        CmsPostPageResult cmsPostPageResult = this.cmspageClient.postPageQuick(cmsPage);
        if (cmsPostPageResult.isSuccess()) {
            //发布成功更新课程状态为已发布
            saveCoursePubState(courseId);
            return new CoursePublishResult(CommonCode.SUCCESS, cmsPostPageResult.getPageUrl());
        }
        return new CoursePublishResult(CommonCode.FAIL,null);
    }

    /**
     * 更新课程状态
     * @param courseId
     */
    private void saveCoursePubState(String courseId) {
        Optional<CourseBase> courseBaseOptional = this.courseBaseRepository.findById(courseId);
        if (courseBaseOptional.isPresent()) {
            CourseBase courseBase = courseBaseOptional.get();
            //更新状态
            courseBase.setStatus("202002");
            this.courseBaseRepository.save(courseBase);
        }
    }
}
