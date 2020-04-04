package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms.service.PageService;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 * @author gakki
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class CmsPageRepositoryTest {

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    private PageService pageService;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Test
    public void testFindAll() {
        List<CmsPage> all = this.cmsPageRepository.findAll();
        System.out.println(all);
    }

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testTemplate() {
        String pageHtml = this.pageService.getPageHtml("5a795ac7dd573c04508f3a56");
        System.out.println(pageHtml);
    }

    // 文件存储2
    @Test
    public void testStore2() throws FileNotFoundException {
        File file = new File("G:\\course.ftl");
        FileInputStream inputStream = new FileInputStream(file);
        //        //保存模版文件内容
        ObjectId objectId = gridFsTemplate.store(inputStream, "课程详情模板文件", "");
        String fileId = objectId.toString();
        System.out.println(fileId);
    }
}
