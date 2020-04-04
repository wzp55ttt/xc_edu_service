package com.xuecheng.manage_cms_client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author gakki
 */
@SpringBootApplication
@EntityScan("com.xuecheng.framework.domain.cms")
@ComponentScan(basePackages={"com.xuecheng.manage_cms_client"})
@ComponentScan(basePackages={"com.xuecheng.framework"})//扫描common工程下的类
public class ManageCnsClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageCnsClientApplication.class);
    }
}
