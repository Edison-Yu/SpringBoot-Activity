package com.yfny.activityapi.service;

import org.activiti.engine.repository.Model;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 流程模型Service
 * <p>
 * Created  by  jinboYu  on  2019/3/28
 */
@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
public interface FlowModelService {

    /**
     * 创建流程模型
     * @param request
     * @param response
     * @throws Exception
     */
    void createModel(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 获取流程模型列表
     * @return
     * @throws Exception
     */
    List<Model> selectModel() throws Exception;

    /**
     * 保存流程模型
     * @param modelId
     * @param name
     * @param description
     * @param json_xml
     * @param svg_xml
     * @return
     */
    int saveModel(@PathVariable String modelId, String name, String description, String json_xml, String svg_xml) throws Exception;

    /**
     * 根据流程模型ID部署流程
     * @param modelId
     * @throws Exception
     */
    int deployModel(String modelId) throws Exception;


}
