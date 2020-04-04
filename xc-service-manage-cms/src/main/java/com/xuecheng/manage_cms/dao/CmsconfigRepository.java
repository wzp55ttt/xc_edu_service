package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author gakki
 */
public interface CmsconfigRepository extends MongoRepository<CmsConfig,String> {
}
