package com.yfny.activityapi.utils;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.zip.ZipInputStream;


/**
 * <p>
 * Created  by  jinboYu  on  2019/3/5
 */
@Component
public class ActivitiUtils {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IdentityService identityService;
    /**
     * 启动流程引擎
     * @param userId    流程发起人ID
     * @param key   流程ID
     * @return
     */
//    public static ProcessInstance getProcessInstance(String userId,String key){
//        if (processInstance==null){
//            synchronized (ProcessInstance.class){
//                if (processInstance==null){
////                    //获取流程引擎对象
//                    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//                    //设置流程发起人
//                    processEngine.getIdentityService().setAuthenticatedUserId(userId);
//                    //启动流程
//                    ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey(key);
//                    return processInstance;
////                    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
////                    Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
////                            .createDeployment()//创建一个部署对象
////                            .name("流程定义")//添加部署名称
////                            .addClasspathResource("processes/xqd.bpmn")//从classpath的资源中加载，一次只能加载一个文件
////                            .addClasspathResource("processes/xqd.png")
////                            .key("xqd")
////                            .deploy();//完成部署
////                    return processEngine.getRuntimeService().startProcessInstanceByKey(deployment.getKey());
//                }else {
//                    return processInstance;
//                }
//            }
//        }else {
//            return processInstance;
//        }
//    }

    /**
     * 启动流程引擎
     *
     * @param userId 流程发起人ID
     * @param key    流程ID
     * @return
     */
    public ProcessInstance getProcessInstance(String userId, String key) {
        //读取配置文件流
        InputStream in = this.getClass().getClassLoader().getSystemResourceAsStream("processes/" + key + ".zip");

        ZipInputStream zipInputStream = new ZipInputStream(in);

        //部署流程实例
        Deployment deployment = repositoryService
                .createDeployment()
                .addZipInputStream(zipInputStream)
                .key(key)
                .deploy();
        identityService.setAuthenticatedUserId(userId);

        //获取流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        //根据流程定义启动流程
        return runtimeService.startProcessInstanceById(processDefinition.getId());
    }


}
