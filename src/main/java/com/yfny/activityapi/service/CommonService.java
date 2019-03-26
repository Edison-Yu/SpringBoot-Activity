package com.yfny.activityapi.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * <p>
 * Created  by  jinboYu  on  2019/3/26
 */
@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
public interface CommonService {

    String createTask(String userId, String key, Map<String,Object> variables) throws Exception;

    String fulfilTask(String taskId, Map<String,Object> variables) throws Exception;

    int revocationTask(String taskId) throws Exception;

    int createUser(String userId) throws Exception;
}
