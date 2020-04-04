package com.xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

/**
 * @author gakki
 */
@Service
public class CmsPageService {

    public static final Logger LOGGER = LoggerFactory.getLogger(CmsPageService.class);

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    GridFsTemplate gridFsTemplate;

    /**
     * 将页面html保存到静态页面物理路径
     * @param pageId
     */
    public void savePageToServerPath(String pageId) {
        //获取页面
        CmsPage cmsPage = this.getCmsPage(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //获取页面id
        String fileId = cmsPage.getHtmlFileId();
        //获取html文件
        InputStream inputStream = this.getFileById(fileId);
        //判断是否存在
        if (inputStream == null) {
            LOGGER.error("getFileById InputStream is null htmlId: " + fileId);
        }

        //查询站点
        CmsSite site = this.getSite(cmsPage.getSiteId());
        //得到站点的物理路径
        String sitePhysicalPath = site.getSitePhysicalPath();
        //页面的物理路径
        String pagePhysicalPath = sitePhysicalPath + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();
        //将html文件保存到服务器物理路径上
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(pagePhysicalPath));
            IOUtils.copy(inputStream, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据文件id获取内容
     *
     * @param id
     * @return
     */
    public InputStream getFileById(String id) {
        try {
            GridFSFile gridFile = this.gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(id)));
            GridFSDownloadStream gridFSDownloadStream = this.gridFSBucket.openDownloadStream(gridFile.getObjectId());
            GridFsResource gridFsResource = new GridFsResource(gridFile, gridFSDownloadStream);
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CmsPage getCmsPage(String id) {
        Optional<CmsPage> byId = this.cmsPageRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        LOGGER.error("cmsPage为null");
        return null;
    }

    public CmsSite getSite(String id) {
        Optional<CmsSite> byId = this.cmsSiteRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }
}
