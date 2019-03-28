package com.yfny.activityapi;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 */
//这里要屏蔽SecurityAutoConfiguration.class,不然登陆Activity-Modeler的时候要输入账号密码
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ActivitiyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitiyApiApplication.class, args);
    }

}
