package com.medical.interceptor;


import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * 自定义拦截器
 * */
@Slf4j
public class GlobalLoggingInterceptor implements HandlerInterceptor {

    /**
     * 前置请求拦截器，请求进入控制器之前会先经过该方法
     * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("\n==================================请求开始==================================");
        log.info(String.format("\n请求信息：\nURL=%s \nMETHOD=%s", request.getServletPath(), request.getMethod()));
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * 在请求完成之后执行，包括视图渲染完成
     * */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("\n==================================请求结束==================================");
    }

}
