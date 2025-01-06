package com.bicycle.advice;

import com.bicycle.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * 全局响应拦截器
 * */
@Slf4j
@ControllerAdvice
public class ResponseInterceptor implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body instanceof AjaxResult) {
            AjaxResult<Object> result = (AjaxResult<Object>) body;
            log.info(String.format("\n响应结果：\nCODE=%s\nMESSAGE=%s\nDATA=%s", result.getCode(), result.getMessage(), result.getData()));
        }
        return body;
    }
}
