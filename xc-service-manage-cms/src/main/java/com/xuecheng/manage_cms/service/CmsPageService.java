package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.course.response.CmsPostPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitConfig;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


/**
 * @author gakki
 */
@Service
public class CmsPageService {

    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    private PageService pageService;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    CmsSiteRepository cmsSiteRepository;

    /**
     *
     * @param page 页数从1开始
     * @param size
     * @param queryPageRequest
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }

        if (page <= 0) {
            page = 1;
        }
        if (size <= 0) {
            size = 10;
        }

        //添加查询条件
        CmsPage cmsPage = new CmsPage();
        if (!StringUtils.isEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }

        if (!StringUtils.isEmpty(queryPageRequest.getPageId())) {
            cmsPage.setPageId(queryPageRequest.getPageId());
        }

        if (!StringUtils.isEmpty(queryPageRequest.getPageType())) {
            String type = queryPageRequest.getPageType();
            if ("静态".equals(type)) {
                type = "0";
            } else if ("动态".equals(type)) {
                type = "1";
            }
            cmsPage.setPageName(type);
        }

        if (!StringUtils.isEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }

        if (!StringUtils.isEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        Example<CmsPage> example = null;
        String pageName = queryPageRequest.getPageName();
        if (!StringUtils.isEmpty(pageName)) {
            ExampleMatcher exampleMatcher = ExampleMatcher.matching().withMatcher(pageName, ExampleMatcher.GenericPropertyMatchers.startsWith());
            example = Example.of(cmsPage, exampleMatcher);
        } else {
            example = Example.of(cmsPage);
        }

        Pageable pageable = PageRequest.of(page - 1, size);

        //查询list
        Page<CmsPage> pages = this.cmsPageRepository.findAll(example,pageable);
        //创建结果集对象
        QueryResult<CmsPage> queryResult = new QueryResult<>();
        //获取结果集合
        List<CmsPage> cmsPageList = pages.getContent();
        //判断结果是否为null
        if (CollectionUtils.isEmpty(cmsPageList)) {
            return new QueryResponseResult(CommonCode.FAIL,new QueryResult());
        }
        //fill
        queryResult.setList(cmsPageList);
        queryResult.setTotal(pages.getTotalElements());
        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }

    public CmsPageResult add(CmsPage cmsPage) {
        if (cmsPage == null) {
            //抛出异常
        }
        //先判断存不存在
        CmsPage cms = this.cmsPageRepository.findByPageNameAndPageWebPathAndSiteId
                (cmsPage.getPageName(),cmsPage.getPageWebPath(),cmsPage.getSiteId());
        if (cms != null) {
            //抛出异常
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        //不存在
        cmsPage.setPageId(null);
        CmsPage cmsPage1 = this.cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS, cmsPage1);
    }

    public CmsPage findById(String id) {
        Optional<CmsPage> cms = this.cmsPageRepository.findById(id);
        if (cms.isPresent()) {
            CmsPage cmsPage = cms.get();
            return cmsPage;
        }
        return null;
    }

    public CmsPageResult update(String id, CmsPage cmsPage) {
        //先根据id查询判断是否存在
        CmsPage page = this.findById(id);
        if (page != null) {
            page.setPageName(cmsPage.getPageName());
            page.setPageAliase(cmsPage.getPageAliase());
            page.setTemplateId(cmsPage.getTemplateId());
            page.setSiteId(cmsPage.getSiteId());
            page.setPageWebPath(cmsPage.getPageWebPath());
            page.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            page.setDataUrl(cmsPage.getDataUrl());
            //更新
            CmsPage save = this.cmsPageRepository.save(page);
            if (save != null) {
                return new CmsPageResult(CommonCode.SUCCESS, save);
            }
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    public ResponseResult delete(String id) {
        CmsPage byId = this.findById(id);
        if (byId == null) {
            return new ResponseResult(CommonCode.FAIL);
        }
        this.cmsPageRepository.deleteById(id);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 页面发布
     * @param pageId
     * @return
     */
    public ResponseResult postPage(String pageId) {
        //执行静态化
        String html = this.pageService.getPageHtml(pageId);
        if (StringUtils.isEmpty(html)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        //保存到grid
        CmsPage cmsPage = this.saveHtml(pageId,html);
        //发送消息
        sendPostPage(pageId);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 发布页面消息到rabbitMQ
     * @param pageId
     */
    private void sendPostPage(String pageId) {
        Optional<CmsPage> byId = this.cmsPageRepository.findById(pageId);
        if (!byId.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("pageId", pageId);
        //消息内容
        String msg = JSON.toJSONString(map);
        //获取站点id作为routingKey
        String siteId = byId.get().getSiteId();
        //发布消息
        this.rabbitTemplate.convertAndSend(RabbitConfig.EX_ROUTING_CMS_POSTPAGE,siteId,msg);
    }

    /**
     * 保存html文件到grid
     * @param pageId
     * @param html
     * @return
     */
    private CmsPage saveHtml(String pageId, String html) {
        Optional<CmsPage> byId = this.cmsPageRepository.findById(pageId);
        if (!byId.isPresent()) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        CmsPage cmsPage = byId.get();
        String htmlFileId = cmsPage.getHtmlFileId();
        if (StringUtils.isNotEmpty(htmlFileId)) {
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(htmlFileId)));
        }
        //html转化为input流
        InputStream stream = null;
        try {
            stream = IOUtils.toInputStream(html, "utf-8");
            ObjectId objectId = this.gridFsTemplate.store(stream, cmsPage.getPageName());
            //文件id
            String id = objectId.toString();
            cmsPage.setHtmlFileId(id);
            this.cmsPageRepository.save(cmsPage);
            return cmsPage;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 保存页面，有则更新，无则新增
     * @param cmsPage
     * @return
     */
    public CmsPageResult save(CmsPage cmsPage) {
        CmsPage cmsPage1 = this.cmsPageRepository.findByPageNameAndPageWebPathAndSiteId(cmsPage.getPageName(),
                cmsPage.getPageWebPath(), cmsPage.getSiteId());
        if (cmsPage1 != null) {
            //更新
            return this.update(cmsPage1.getPageId(), cmsPage);
        }
        //新增
        return this.add(cmsPage);
    }

    /**
     * 一键发布
     * @param cmsPage
     * @return
     */
    public CmsPostPageResult postPageQuick(CmsPage cmsPage) {
        CmsPageResult save = this.save(cmsPage);
        if (!save.isSuccess()) {
            //保存失败
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //发布
        String siteId = save.getCmsPage().getSiteId();
        CmsPage cmsPage1 = save.getCmsPage();
        String pageId = cmsPage1.getPageId();
        ResponseResult responseResult = this.postPage(pageId);
        //再次查询

        if (!responseResult.isSuccess()) {
            //发布失败
            ExceptionCast.cast(CommonCode.FAIL);
        }
        Optional<CmsSite> cmsSiteOptional = this.cmsSiteRepository.findById(siteId);
        if (cmsSiteOptional.isPresent()) {
            //拼装文件web路径
            CmsSite cmsSite = cmsSiteOptional.get();
            String url = cmsSite.getSiteDomain() + cmsSite.getSiteWebPath() + cmsPage1.getPageWebPath() + cmsPage1.getPageName();
            return new CmsPostPageResult(CommonCode.SUCCESS, url);
        }
        return new CmsPostPageResult(CommonCode.FAIL,null);
    }
}
