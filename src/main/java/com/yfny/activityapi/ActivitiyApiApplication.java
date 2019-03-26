package com.yfny.activityapi;

import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类
 */
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ActivitiyApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivitiyApiApplication.class, args);
    }

}
