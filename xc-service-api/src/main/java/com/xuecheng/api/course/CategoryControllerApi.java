package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author gakki
 */
@Api(value = "课程分类")
public interface CategoryControllerApi {

    @ApiOperation("查询所有课程")
    public CategoryNode findAll();
}
