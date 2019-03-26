package com.yfny.activityapi.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理机制
 * Created by jisongZhou on 2019/2/18.
 **/

@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String handException(HttpServletRequest req, Exception e) throws Exception {
        logger.error("报错信息:" + e.getMessage(), e);
        return "10005," + e.getMessage();
    }
}
