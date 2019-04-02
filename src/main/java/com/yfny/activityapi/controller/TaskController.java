package com.yfny.activityapi.controller;

import com.yfny.activityapi.service.FlowTaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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



    @Autowired
    private FlowTaskService flowTaskService;


    /**
     * 根据分组ID获取任务列表，带分页
     *
     * @param groupId  分组ID
     * @param pageNum  当前页数
     * @param pageSize 显示数量
     * @return
     */
    @GetMapping(value = "/getDemandByGroupId/{groupId}/{pageNum}/{pageSize}")
    public String getDemandByGroupId(@PathVariable String groupId, @PathVariable int pageNum, @PathVariable int pageSize) throws Exception {
        return flowTaskService.getDemandByGroupId(groupId,pageNum,pageSize);
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
    public String getDemandByUserId(@PathVariable String userId, @PathVariable int pageNum, @PathVariable int pageSize) throws Exception {
        return flowTaskService.getDemandByUserId(userId,pageNum,pageSize);
    }

    @GetMapping(value = "/getTaskListByUserId/{userId}/{pageNum}/{pageSize}")
    public List<Task> getTaskListByUserId(@PathVariable String userId, @PathVariable int pageNum, @PathVariable int pageSize) throws Exception {
        return flowTaskService.getTaskListByUserId(userId,pageNum,pageSize);
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
        } else if (i==2){
            return "撤销失败,任务不存在";
        }else {
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