package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.CourseMarketRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author gakki
 */
@Service
public class CourseMarketService {

    @Autowired
    CourseMarketRepository courseMarketRepository;


    public CourseMarket findCourseMarkById(String courseId) {
        Optional<CourseMarket> market = this.courseMarketRepository.findById(courseId);
        if (market.isPresent()) {
            return market.get();
        }
        return null;
    }

    public ResponseResult update(String courseId, CourseMarket courseMarket) {
        CourseMarket market = this.findCourseMarkById(courseId);
        if (market == null) {
            market = new CourseMarket();
            BeanUtils.copyProperties(courseMarket, market);
            market.setId(courseId);
        } else {
            market.setCharge(courseMarket.getCharge());
            market.setValid(courseMarket.getValid());
            market.setEndTime(courseMarket.getEndTime());
            market.setQq(courseMarket.getQq());
            market.setPrice_old(market.getPrice());
            market.setPrice(courseMarket.getPrice());
            market.setStartTime(courseMarket.getStartTime());
            market.setEndTime(courseMarket.getEndTime());
        }
        CourseMarket courseMarket1 = this.courseMarketRepository.save(market);
        if (courseMarket1 != null) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }
}
