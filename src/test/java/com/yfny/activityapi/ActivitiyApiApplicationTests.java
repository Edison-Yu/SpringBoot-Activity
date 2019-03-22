package com.yfny.activityapi;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiyApiApplicationTests {

    @Test
    public void contextLoads() {
    }

    /**
     * 创建分组
     */
    @Test
    public void createGroup(){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService is = engine.getIdentityService();
        for (int i=0;i<10;i++){
            Group group =  is.newGroup(String.valueOf(i));
            group.setName("groupName_"+i);
            group.setType("groupType_"+i);
            is.saveGroup(group);
        }
        engine.close();
    }

    /**
     * 自定义条件查询
     */
    @Test
    public void selectGroup(){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService is = engine.getIdentityService();
        List<Group> groups = is.createNativeGroupQuery().sql("SELECT * FROM act_id_group WHERE NAME_ LIKE #{name}").parameter("name","%groupName%").list();
        System.out.println(groups.get(0).getName());
    }

}
