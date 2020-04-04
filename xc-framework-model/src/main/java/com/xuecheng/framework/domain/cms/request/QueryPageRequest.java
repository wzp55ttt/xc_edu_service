package com.xuecheng.framework.domain.cms.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author HP
 */
@Data
public class QueryPageRequest {

    /**站点id*/
    /**接收页面查询的查询条件*/
    @ApiModelProperty("站点id")
    private String siteId;

    //页面id
    private String pageId;

    //页面名称
    private String pageName;

    //别名
    private String pageAliase;

    //模板id
    private String templateId;

    //页面类型
    private String pageType;


}
