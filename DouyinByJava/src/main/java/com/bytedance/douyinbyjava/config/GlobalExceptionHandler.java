package com.bytedance.douyinbyjava.config;

import com.bytedance.douyinbyjava.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseDto<String> exceptionHandler (SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());

        if (ex.getMessage().contains("Duplicate entry")) {
            String s = ex.getMessage().split(" ")[2];
            return ResponseDto.failure(s + "已存在");
        }

        return ResponseDto.failure("由于未知错误导致添加失败！");
    }

    @ExceptionHandler(CustomException.class)
    public ResponseDto<String> exceptionHandler (CustomException ex) {
        log.error(ex.getMessage());

        return ResponseDto.failure(ex.getMessage());
    }
}
