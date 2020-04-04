package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.manage_cms_client.service.CmsPageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author gakki
 */
@Component
public class ConsumerPostPage {

    @Autowired
    private CmsPageService cmsPageService;

    @RabbitListener(queues = "${xuecheng.mq.queue}")
    public void postPage(String msg) {
        //解析信息
        Map map = JSON.parseObject(msg, Map.class);
        //得到消息中的页面id
        String pageId = (String) map.get("pageId");
        this.cmsPageService.savePageToServerPath(pageId);
    }
}
