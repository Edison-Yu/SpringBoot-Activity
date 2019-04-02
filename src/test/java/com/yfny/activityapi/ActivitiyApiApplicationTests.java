package com.yfny.activityapi;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.Group;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
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

    @Test
    public void readFileByLines() {
        File file = new File("D:/zz.txt");
        //判断文件是否存在
        if (!file.exists()) {
            System.out.println("文件夹不存在");
        }else {
            BufferedReader reader = null;
            StringBuffer sb = new StringBuffer();
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                String tempString = null;
                while ((tempString = reader.readLine()) != null) {
                    int i = sb.lastIndexOf("}");

                    sb.append(tempString);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
            }
            System.out.println("读取成功");
        }
    }

    @Test
    public void getTask(){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = engine.getHistoryService();
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId("17507").singleResult();
    }

}
