package com.example.sp20250610.exception;

import com.example.sp20250610.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice("com.example.sp20250611.controller")
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        log.error("系统异常",e);
        return Result.error("error500","系统异常");
    }


    @ExceptionHandler(CustomerException.class)
    @ResponseBody
    public Result customerError(CustomerException e) {
        log.error("自定义错误",e);
        return Result.error(e.getCode(),e.getMsg());
    }
}
