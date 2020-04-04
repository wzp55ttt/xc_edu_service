package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author gakki
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {

    /**
     * 判断唯一性
     * @param
     * @return
     */
    CmsPage findByPageNameAndPageWebPathAndSiteId(String pageName,String pageWebPath,String siteId);

}
