package com.yfny.activityapi.service.impl;

import com.yfny.activityapi.service.FlowTaskService;
import com.yfny.activityapi.utils.ActivitiUtils;
import com.yfny.activityapi.utils.DeleteTaskCmd;
import com.yfny.activityapi.utils.SetFLowNodeAndGoCmd;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 流程任务Service实现类
 * <p>
 * Created  by  jinboYu  on  2019/3/11
 */
@Service
public class FlowTaskServiceImpl implements FlowTaskService {

    @Autowired
    private ActivitiUtils activitiUtils;

    @Autowired
    private org.activiti.engine.TaskService taskService;

    @Autowired
    private ManagementService managementService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private IdentityService identityService;


    /**
     * 创建任务
     *
     * @param userId
     * @param key
     * @param variables
     * @return
     */
    public String createTask(String userId, String key, Map<String, Object> variables) throws Exception {
        //获取当前流程实例ID
        String processInstanceId = activitiUtils.getProcessInstance(userId, key).getId();
        //查询第一个任务
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        //设置流程任务变量
        taskService.setVariables(task.getId(), variables);
        //完成任务
        taskService.complete(task.getId());
        //返回下一个任务的ID
        String taskId = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult().getId();
        return "任务ID:" + taskId + ",流程实例ID:" + processInstanceId;
    }

    /**
     * 完成任务
     *
     * @param taskId    任务ID
     * @param variables 流程变量
     * @return
     */
    public String fulfilTask(String taskId, Map<String, Object> variables) {
        //根据任务ID获取当前任务实例
        Task task = this.taskService.createTaskQuery().taskId(taskId).singleResult();
        //设置流程任务变量
        taskService.setVariables(taskId, variables);
        //完成任务
        taskService.complete(taskId);
        //返回下一个任务的ID
        taskId = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).singleResult().getId();
        return "任务ID:" + taskId + ",流程实例ID:" + task.getProcessInstanceId();
    }

    /**
     * 取消任务
     *
     * @param taskId 任务ID
     * @return
     */
    public int revocationTask(String taskId) {
        //获取当前任务
        Task currentTask = taskService.createTaskQuery().taskId(taskId).singleResult();
        //获取流程定义
        Process process = repositoryService.getBpmnModel(currentTask.getProcessDefinitionId()).getMainProcess();
        //获取目标节点定义
        FlowNode targetNode = (FlowNode) process.getFlowElement("endevent1");
        //删除当前运行任务
        String executionEntityId = managementService.executeCommand(new DeleteTaskCmd(currentTask.getId()));
        //流程执行到来源节点
        managementService.executeCommand(new SetFLowNodeAndGoCmd(targetNode, executionEntityId));
        return 1;
    }

    /**
     * 创建用户
     *
     * @param userId
     * @return
     */
    public int createUser(String userId) {
        User user = identityService.newUser(userId);
        user.setFirstName("edison" + userId);
        user.setLastName("yu");
        user.setId(userId);
        identityService.saveUser(user);
        return 1;
    }
}
