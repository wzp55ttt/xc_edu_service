package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.manage_course.client.CmspageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.sound.midi.Soundbank;
import java.util.Optional;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {
    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseMapper courseMapper;

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Test
    public void testCourseBaseRepository(){
        Optional<CourseBase> optional = courseBaseRepository.findById("402885816240d276016240f7e5000002");
        if(optional.isPresent()){
            CourseBase courseBase = optional.get();
            System.out.println(courseBase);
        }

    }

    @Test
    public void testCourseMapper(){
//        CourseBase courseBase = courseMapper.findCourseBaseById("402885816240d276016240f7e5000002");
//        System.out.println(courseBase);

    }

    @Test
    public void test() {
        TeachplanNode teachplanNode = this.teachplanMapper.selectList("402885816240d276016240f7e5000002");
        System.out.println("");
    }

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    CmspageClient cmspageClient;

    @Test
    public void test1() {
        CmsPageResult byId = cmspageClient.findById("5a795ac7dd573c04508f3a56");
        System.out.println(byId);
    }
}

















