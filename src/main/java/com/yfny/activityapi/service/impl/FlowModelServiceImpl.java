package com.yfny.activityapi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yfny.activityapi.service.FlowModelService;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 流程模型Service实现类
 * <p>
 * Created  by  jinboYu  on  2019/3/28
 */
@Service
public class FlowModelServiceImpl implements FlowModelService {

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public void createModel(HttpServletRequest request, HttpServletResponse response) throws Exception{

        String modelName = "modelName";
        String modelKey = "modelKey";
        String description = "description";

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        Model modelData = repositoryService.newModel();

        ObjectNode modelObjectNode = objectMapper.createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, modelName);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        modelData.setMetaInfo(modelObjectNode.toString());
        modelData.setName(modelName);
        modelData.setKey(modelKey);

        //保存模型
        repositoryService.saveModel(modelData);
        repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
        response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());

    }

    @Override
    public List<Model> selectModel() throws Exception {
        List<Model> modeList = repositoryService.createModelQuery().list();
        if (modeList != null && modeList.size() > 0) {
            return modeList;
        }else {
            return null;
        }
    }
}
