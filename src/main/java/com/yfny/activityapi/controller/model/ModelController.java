package com.yfny.activityapi.controller.model;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yfny.activityapi.service.ModelService;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 流程模型Controller
 * <p>
 * Created  by  jinboYu  on  2019/3/28
 */
@RestController
@RequestMapping(value = "/model")
public class ModelController {

    @Autowired
    private ModelService modelService;


    /**
     * 创建流程模型
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/create")
    public void createModel(HttpServletRequest request, HttpServletResponse response) throws Exception{
        modelService.createModel(request,response);
    }


    @GetMapping(value="/{modelId}/json", produces = "application/json")
    public ObjectNode getEditorJson(@PathVariable String modelId) throws Exception {
        return modelService.getEditorJson(modelId);
    }

    /**
     * 获取流程模型,带分页
     * @param pageNum   当前页
     * @param pageSize
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/selectModel/{pageNum}/{pageSize}")
    public List<Model> selectModel(@PathVariable int pageNum,@PathVariable int pageSize) throws Exception{
        return modelService.selectModel(pageNum,pageSize);
    }

    /**
     * 保存流程模型
     * @param modelId
     * @param name
     * @param description
     * @param json_xml
     * @param svg_xml
     * @return
     * @throws Exception
     */
    @PutMapping(value="/{modelId}/save")
    @ResponseStatus(value = HttpStatus.OK)
    public int saveModel(@PathVariable String modelId, String name, String description, String json_xml, String svg_xml) throws Exception {
        return modelService.saveModel(modelId,name,description,json_xml,svg_xml);
    }

    /**
     * 部署流程模型
     * @param modelId
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/deployModel/{modelId}")
    public int deployModel(@PathVariable String modelId) throws Exception {
        return modelService.deployModel(modelId);
    }

}
