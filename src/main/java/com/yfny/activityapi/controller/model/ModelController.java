package com.yfny.activityapi.controller.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yfny.activityapi.service.FlowModelService;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private FlowModelService flowModelService;


    @RequestMapping("/create")
    public void createModel(HttpServletRequest request, HttpServletResponse response) throws Exception{
        flowModelService.createModel(request,response);
    }

    //获取所有流程模型
    @GetMapping(value = "/selectModel")
    public List<Model> selectModel() throws Exception{
        return flowModelService.selectModel();
    }

}
