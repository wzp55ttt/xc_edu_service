package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author gakki
 */
@Api(value = "课程等级")
public interface SysDictionaryControllerApi {

    @ApiOperation("查询课程等级")
    public SysDictionary findByType(String type);
}
