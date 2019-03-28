package com.yfny.activityapi.controller;

import com.yfny.activityapi.service.FlowTaskService;
import com.yfny.activityapi.utils.ActivitiUtils;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程任务Controller
 * <p>
 * Created  by  jinboYu  on  2019/3/5
 */
@RestController
@RequestMapping(value = "/task")
public class TaskController {


    private TaskService taskService;

    private HistoryService historyService;

    @Autowired
    private FlowTaskService flowTaskService;

    //流程任务Service
    @Autowired
    private ActivitiUtils activitiUtils;

    /**
     * 根据分组ID获取任务列表，带分页
     *
     * @param groupId  分组ID
     * @param pageNum  当前页数
     * @param pageSize 显示数量
     * @return
     */
    @GetMapping(value = "/getDemandByGroupId/{groupId}/{pageNum}/{pageSize}")
    public String getDemandByGroupId(@PathVariable String groupId, @PathVariable int pageNum, @PathVariable int pageSize) {
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

    /**
     * 根据当前用户Id获取任务列表,带分页
     *
     * @param userId   用户ID
     * @param pageNum  当前页数
     * @param pageSize 显示数量
     * @return
     */
    @GetMapping(value = "/getDemandByUserId/{userId}/{pageNum}/{pageSize}")
    public String getDemandByUserId(@PathVariable String userId, @PathVariable int pageNum, @PathVariable int pageSize) {
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

    @GetMapping(value = "/getTaskListByUserId/{userId}/{pageNum}/{pageSize}")
    public List<Task> getTaskListByUserId(@PathVariable String userId, @PathVariable int pageNum, @PathVariable int pageSize) {
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


    /**
     * 创建流程并完成第一个任务
     *
     * @param userId    流程发起人ID
     * @param key       流程ID
     * @param variables 流程变量
     * @return 下一个任务的ID
     */
    @PostMapping(value = "/create/{userId}/{key}")
    public String createTask(@PathVariable String userId, @PathVariable String key, @RequestBody Map<String, Object> variables) throws Exception {
        String taskId = flowTaskService.createTask(userId, key, variables);
        return taskId;
    }

    /**
     * 完成流程任务
     *
     * @param taskId    任务ID
     * @param variables 流程变量
     * @return 返回下一个任务的ID
     */
    @PostMapping(value = "/fulfil/{taskId}")
    public String fulfilTask(@PathVariable String taskId, @RequestBody Map<String, Object> variables) throws Exception {
        return flowTaskService.fulfilTask(taskId, variables);
    }

    /**
     * 创建流程并获取第一个任务
     *
     * @param userId    创建人ID
     * @param key       流程ID
     * @param variables 流程变量
     * @return 返回当前任务的ID
     */
    @PostMapping(value = "/createFlow/{userId}/{key}")
    public String createFlow(@PathVariable String userId, @PathVariable String key, @RequestBody Map<String, Object> variables) throws Exception {
        //获取当前流程实例ID
        String processInstanceId = activitiUtils.getProcessInstance(userId, key).getId();
        //查询第一个任务
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        //设置流程任务变量
        taskService.setVariables(task.getId(), variables);
        return task.getId();
    }


    /**
     * 撤销流程
     *
     * @param taskId 流程任务ID
     * @return
     */
    @PostMapping(value = "/revocationTask/{taskId}")
    public String revocationTask(@PathVariable String taskId) throws Exception {
        int i = flowTaskService.revocationTask(taskId);
        if (i == 1) {
            return "撤销成功";
        } else {
            return "撤销失败";
        }
    }

    /**
     * 创建用户
     *
     * @param userId
     * @return
     */
    @PostMapping(value = "/createUser/{userId}")
    public String createUser(@PathVariable String userId) throws Exception {
        int i = flowTaskService.createUser(userId);
        if (i == 1) {
            return "创建成功";
        }
        return "创建失败";
    }
}