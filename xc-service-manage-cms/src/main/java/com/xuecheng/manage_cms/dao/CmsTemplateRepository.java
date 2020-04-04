package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author gakki
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {
}
