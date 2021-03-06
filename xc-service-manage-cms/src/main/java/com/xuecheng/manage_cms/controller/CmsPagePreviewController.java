package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.PageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * @author gakki
 */
@Controller
public class CmsPagePreviewController extends BaseController {

    @Autowired
    private PageService pageService;

    /**
     * 页面预览
     * @param pageId
     */
    @GetMapping("/cms/preview/{pageId}")
    public void preview(@PathVariable("pageId")String pageId) {
        String pageHtml = this.pageService.getPageHtml(pageId);
        if (StringUtils.isNotEmpty(pageHtml)) {
            try {
                ServletOutputStream outputStream = response.getOutputStream();
                response.setHeader("Content-type","text/html;charset=utf-8");
                outputStream.write(pageHtml.getBytes("utf-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
