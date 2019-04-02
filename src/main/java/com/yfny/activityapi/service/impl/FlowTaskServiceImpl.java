package com.yfny.activityapi.service.impl;

import com.yfny.activityapi.service.FlowTaskService;
import com.yfny.activityapi.utils.ActivitiUtils;
import com.yfny.activityapi.utils.DeleteTaskCmd;
import com.yfny.activityapi.utils.SetFLowNodeAndGoCmd;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private HistoryService historyService;


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
        if (task!=null){
            //设置流程任务变量
            taskService.setVariables(taskId, variables);
            //完成任务
            taskService.complete(taskId);
            //获取下一个任务
            Task nextTask = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
            //如果还存在下一任务
            if (nextTask!=null){
                return "任务ID:" + nextTask.getId() + ",流程实例ID:" + task.getProcessInstanceId();
            }
            //否则
            else {
                HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
                if (historicProcessInstance!=null && historicProcessInstance.getEndActivityId()!=null){
                    return "流程结束";
                }else {
                    return "出错了呀";
                }
            }
        }else {
            return "任务不存在";
        }


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
        if (currentTask!=null){
            //获取流程定义
            Process process = repositoryService.getBpmnModel(currentTask.getProcessDefinitionId()).getMainProcess();
            //获取目标节点定义
            FlowNode targetNode = (FlowNode) process.getFlowElement("endevent1");
            //删除当前运行任务
            String executionEntityId = managementService.executeCommand(new DeleteTaskCmd(currentTask.getId()));
            //流程执行到来源节点
            managementService.executeCommand(new SetFLowNodeAndGoCmd(targetNode, executionEntityId));
            return 1;
        }else {
            return 2;
        }

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

    /**
     * 根据用户ID查询任务,带分页
     *
     * @param userId   用户ID
     * @param pageNum  当前页
     * @param pageSize 显示数量
     * @return
     * @throws Exception
     */
    @Override
    public String getDemandByUserId(String userId, int pageNum, int pageSize) throws Exception {
        pageNum = (pageNum - 1) * pageSize;
        List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery().includeProcessVariables().startedBy(userId).listPage(pageNum, pageSize);
        if (historicProcessInstanceList != null && historicProcessInstanceList.size() > 0) {
            for (HistoricProcessInstance historicProcessInstance : historicProcessInstanceList) {
                Task task = taskService.createTaskQuery().processInstanceId(historicProcessInstance.getId()).includeProcessVariables().singleResult();

                System.out.println("任务ID：" + task.getId());
            }
            return "获取成功，该组织下任务数:" + historicProcessInstanceList.size();
        } else {
            return "获取成功，该组织下任务书: 0 ";
        }
    }

    /**
     * 根据分组ID获取任务列表，带分页
     *
     * @param groupId  分组ID
     * @param pageNum  当前页数
     * @param pageSize 显示数量
     * @return
     */
    @Override
    public String getDemandByGroupId(String groupId, int pageNum, int pageSize) throws Exception {
        pageNum = (pageNum - 1) * pageSize;
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        //根据分组ID获取任务列表，不包含流程变量
//        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(groupId).listPage(pageNum,pageSize);
        //根据分组ID获取任务列表，包含流程变量
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(groupId).includeProcessVariables().listPage(pageNum, pageSize);
        if (tasks != null && tasks.size() > 0) {
            for (Task task : tasks) {
                Map<String, Object> resultMap = new HashMap<String, Object>();
                resultMap.put("任务名称", task.getName());
                resultMap.put("任务ID", task.getId());
                resultMap.put("组织ID", task.getProcessVariables().get("zzid"));
                resultMap.put("创建人", task.getProcessVariables().get("createName"));
                resultMap.put("需求描述", task.getProcessVariables().get("demandReviews"));
                resultList.add(resultMap);
            }
            JSONArray jsonArray = new JSONArray(resultList.toString());
            return jsonArray.toString();
        } else {
            return "获取成功，该组织下任务数: 0 ";
        }
    }

    @Override
    public List<Task> getTaskListByUserId(String userId, int pageNum, int pageSize) throws Exception {
        List<Task> taskList = new ArrayList<Task>();
        pageNum = (pageNum - 1) * pageSize;
        List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery().includeProcessVariables().startedBy(userId).listPage(pageNum, pageSize);
        if (historicProcessInstanceList != null && historicProcessInstanceList.size() > 0) {
            for (HistoricProcessInstance historicProcessInstance : historicProcessInstanceList) {
                Task task = taskService.createTaskQuery().processInstanceId(historicProcessInstance.getId()).includeProcessVariables().singleResult();
                taskList.add(task);
            }
            return taskList;
        } else {
            return taskList;
        }
    }
}
