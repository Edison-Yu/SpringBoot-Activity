package com.yfny.activityapi.service;

import org.activiti.engine.task.Task;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 流程任务Service
 * <p>
 * Created  by  jinboYu  on  2019/3/26
 */
@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
public interface FlowTaskService {

    /**
     * 创建任务
     * @param userId    任务创建人ID
     * @param key       部署流程ID
     * @param variables 任务变量
     * @return          返回任务ID
     * @throws Exception
     */
    String createTask(String userId, String key, Map<String,Object> variables) throws Exception;

    /**
     * 完成任务
     * @param taskId    任务ID
     * @param variables 任务变量
     * @return          返回任务ID
     * @throws Exception
     */
    String fulfilTask(String taskId, Map<String,Object> variables) throws Exception;

    /**
     * 取消任务
     * @param taskId    任务ID
     * @return          1为取消成功，2为取消失败
     * @throws Exception
     */
    int revocationTask(String taskId) throws Exception;

    /**
     * 创建用户
     * @param userId    用户ID
     * @return          1为创建成功。2为创建失败
     * @throws Exception
     */
    int createUser(String userId) throws Exception;

    /**
     * 根据用户ID查询任务,带分页
     * @param userId    用户ID
     * @param pageNum   当前页
     * @param pageSize  显示数量
     * @return
     * @throws Exception
     */
    String getDemandByUserId( String userId,int pageNum, int pageSize)throws Exception;

    /**
     * 根据分组ID获取任务列表，带分页
     *
     * @param groupId  分组ID
     * @param pageNum  当前页数
     * @param pageSize 显示数量
     * @return
     */
    String getDemandByGroupId(String groupId,int pageNum,int pageSize)throws Exception;

    List<Task> getTaskListByUserId(String userId, int pageNum, int pageSize)throws Exception;
}
