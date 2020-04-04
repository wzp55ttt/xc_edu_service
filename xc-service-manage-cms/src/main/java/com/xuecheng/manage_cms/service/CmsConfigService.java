package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsconfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author gakki
 */
@Service
public class CmsConfigService {

    @Autowired
    private CmsconfigRepository cmsconfigRepository;


    public CmsConfig findById(String id) {
        Optional<CmsConfig> byId = this.cmsconfigRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }
}
