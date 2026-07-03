package com.example.attendancesystem.config;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public String handleNotFound(Model model) {
        model.addAttribute("error", "页面不存在，请检查访问地址。");
        return "error/404";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handle(Exception e, Model model) {
        model.addAttribute("error", e.getMessage() == null ? "页面处理失败，请返回首页重试" : e.getMessage());
        return "error/500";
    }
}
