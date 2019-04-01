package com.yfny.activityapi.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
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
public interface ModelService {

    /**
     * 创建流程模型
     * @param request
     * @param response
     * @throws Exception
     */
    void createModel(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 获取流程模型,带分页
     * @param pageNum   当前页
     * @param pageSize  显示数量
     * @return
     * @throws Exception
     */
    List<Model> selectModel(int pageNum,int pageSize) throws Exception;

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


    /**
     * 获取模型json数据
     * @param modelId
     * @return
     * @throws Exception
     */
    ObjectNode getEditorJson(@PathVariable String modelId) throws Exception;
}
