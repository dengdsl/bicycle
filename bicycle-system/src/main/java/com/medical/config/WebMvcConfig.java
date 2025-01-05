package com.medical.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.medical.interceptor.GlobalLoggingInterceptor;
import com.medical.interceptor.SaTokenInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Resource
    private SaTokenInterceptor saTokenInterceptor;
    /**
     * 配置允许跨域
     * */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "DELETE", "PUT")
                .maxAge(3600);
    }

    /**
     * 注册自定义拦截器记录请求日志信息
     * */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GlobalLoggingInterceptor()).addPathPatterns("/**");
        // 添加登录拦截器
        registry.addInterceptor(saTokenInterceptor).addPathPatterns("/**");
    }

    /**
     * 静态文件拦截器
     * */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("file:F:\\workspace\\bicyclemanagement\\bicycle-system\\src\\main\\resources\\static\\file\\");
//        registry.addResourceHandler("/static/**").addResourceLocations("/home/server/static/");
    }
}
