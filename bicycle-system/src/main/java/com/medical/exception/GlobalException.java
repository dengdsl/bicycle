package com.medical.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.medical.enums.HttpEnum;
import com.medical.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;


/**
 * 全局异常拦截器处理
 * */
@Slf4j
@ControllerAdvice
public class GlobalException {

    /**
     * 处理所有不可知异常
     * */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public AjaxResult<Object> handleException(Exception e){
        log.error(e.getMessage());
        return AjaxResult.failed(HttpEnum.SYSTEM_ERROR.getCode(), e.getMessage());
    }

    /**
     * 拦截404异常
     * */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ExceptionHandler(NoHandlerFoundException.class)
    public AjaxResult<Object> handleNoHandlerFoundException(NoHandlerFoundException e) {
        return AjaxResult.failed(HttpEnum.REQUEST_404_ERROR.getCode(), HttpEnum.REQUEST_404_ERROR.getMessage());
    }

    /**
     * 拦截自定义异常
     * */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ExceptionHandler(CustomException.class)
    public AjaxResult<Object> handleCustomException(CustomException e) {
        log.error("自定义异常：code=" + e.getCode() + ", message=" + e.getMessage());
        return AjaxResult.failed(e.getCode(), e.getMessage());
    }

    /**
     * 拦截表单参数验证
     * */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ExceptionHandler(BindException.class)
    public AjaxResult<Object> handleBindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        String message = Objects.requireNonNull(bindingResult.getFieldError().getDefaultMessage());
        log.error("表单参数验证异常：" + message);
        return AjaxResult.failed(HttpEnum.PARAMS_VALID_ERROR.getCode(), message);
    }

    /**
     * 拦截请求路径参数校验异常
     * */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public AjaxResult<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e){
        String message = Objects.requireNonNull(e.getMessage());
        log.error("路径参数验证异常：" + message );
        return AjaxResult.failed(HttpEnum.PARAMS_VALID_ERROR.getCode());
    }

    /**
     * 拦截json参数校验异常
     * */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxResult<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        String message = Objects.requireNonNull(bindingResult.getFieldError().getDefaultMessage());
        log.error("JSON参数验证异常：" + message );
        return AjaxResult.failed(HttpEnum.PARAMS_VALID_ERROR.getCode(), message);
    }

    /**
     * 拦截参数类型异常
     * */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public AjaxResult<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e){
        String message = Objects.requireNonNull(e.getMessage());
        log.error("请求参数类型异常：" + message.split(";")[0] );
        return AjaxResult.failed(HttpEnum.PARAMS_TYPE_ERROR.getCode(), message.split(";")[0]);
    }

    /**
     * 拦截请求方法异常
     * */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        String message = Objects.requireNonNull(e.getMessage());
        log.error("请求方式错误：" + message);
        return AjaxResult.failed(HttpEnum.REQUEST_METHOD_ERROR.getCode(), message);
    }

    /**
     * 拦截MybatisPlus异常
     * */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MybatisPlusException.class)
    @ResponseBody
    public AjaxResult<Object> handleMybatisPlusException(MybatisPlusException e) {
        Integer code = HttpEnum.ASSERT_MYBATIS_ERROR.getCode();
        String message   = Objects.requireNonNull(e.getMessage());
        log.error("Mybatis-Plus异常：" + message);
        return AjaxResult.failed(code, message);
    }

    /**
     * 拦截登录校验异常
     * */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotLoginException.class)
    @ResponseBody
    public AjaxResult<Object> handleNotLoginException(NotLoginException e) {
        Integer code = HttpEnum.TOKEN_INVALID.getCode();
        String message   = Objects.requireNonNull(e.getMessage());
        log.error("登录校验异常：" + message);
        return AjaxResult.failed(code, message);
    }

    /**
     * 拦截权限异常
     * */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NotPermissionException.class)
    @ResponseBody
    public AjaxResult<Object> handleNotPermissionException(NotPermissionException e){
        String message = Objects.requireNonNull(e.getMessage());
        log.error("无相关权限:" + message);
        return AjaxResult.failed(HttpEnum.NO_PERMISSION.getCode(), HttpEnum.NO_PERMISSION.getMessage());
    }

}
