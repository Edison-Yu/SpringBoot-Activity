package com.yfny.activityapi.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

/**
 * 生成流程图Service
 * <p>
 * Created  by  jinboYu  on  2019/3/26
 */
@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
public interface FlowService {

    /**
     * 获取历史节点流程图
     * 生成的流程图会高亮历史节点到当前的节点
     * @param taskId 任务ID
     * @return
     */
    InputStream getResourceDiagramInputStream(String taskId);


    /**
     * 根据当前流程实例ID获取流程图片流文件
     * 生成的流程图图片只高亮当前任务的节点
     * @param taskId 任务ID
     * @return
     */
    InputStream getDiagram(String taskId);
}
