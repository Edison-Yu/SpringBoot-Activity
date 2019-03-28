package com.yfny.activityapi.service;

import org.activiti.engine.repository.Model;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    void createModel(HttpServletRequest request, HttpServletResponse response) throws Exception;

    List<Model> selectModel() throws Exception;
}
