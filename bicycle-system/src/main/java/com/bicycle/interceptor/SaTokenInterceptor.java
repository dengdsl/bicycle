package com.bicycle.interceptor;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bicycle.annotation.NotLogin;
import com.bicycle.annotation.NotPower;
import com.bicycle.entry.account.SystemAccountEntry;
import com.bicycle.enums.HttpEnum;
import com.bicycle.exception.LoginException;
import com.bicycle.mapper.account.SystemAccountMapper;
import com.bicycle.utils.AjaxResult;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;


@Component
@Slf4j
public class SaTokenInterceptor implements HandlerInterceptor {

    @Resource
    private SystemAccountMapper userMapper;

    /**
     * 前置处理器,用于判断用户登录权限
     *
     * */
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request,
                             @NotNull HttpServletResponse response,
                             @NotNull Object handler) throws Exception {
        // 判断请求路径前缀是否正确
        String requestURI = request.getRequestURI();
        if (!(handler instanceof HandlerMethod) || !requestURI.startsWith("/bicycle-api/api")) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }
        // 登录权限校验
        try {
            response.setContentType("application/json;charset=utf-8");
            this.checkLogin(this.getAnnotationMethod(handler), requestURI);
        }catch (LoginException e) {
            // 捕获登录异常,将处理结果返回
            AjaxResult<Object> result = new AjaxResult<>(e.getCode(), e.getMessage());
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }
        // 通过验证
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    /**
     * 后置处理器,该处理器中不做任何处理
     * */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    /**
     * 提取注解
     *
     * @param handler 处理器
     * */
    public Method getAnnotationMethod(@NotNull Object handler) throws Exception{
        String[] splitArr = handler.toString().split("#");
        String methodStr = splitArr[1].split("\\(")[0];
        String classStr = splitArr[0];
        // 通过反射加载类
        Class<?> clazz = Class.forName(classStr);
        // 获取类中的方法列表
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            // 如果找到对应的方法就将找到的方法返回
            if (method.getName().equals(methodStr)) {
                return  method;
            }
        }
        // 没有满足条件的方法
        return null;
    }

    /**
     * 登录校验方法
     * @param method 方法
     * @param requestUri 请求地址
     * */
    public void checkLogin(Method method, String requestUri) {
        for (int i = 0; i <= 0; i++) {
            // 免登录校验
            if (method != null && method.isAnnotationPresent(NotLogin.class)) {
                break;
            }
            // 获取token令牌
            String tokenValue = StpUtil.getTokenValue();
            if (tokenValue == null || StringUtils.isBlank(tokenValue)) {
                // token信息不存在
                throw new LoginException(HttpEnum.TOKEN_INVALID.getCode(), HttpEnum.TOKEN_INVALID.getMessage());
            }
            // 登录校验
            Object loginId = StpUtil.getLoginId();
            if (loginId == null) {
                // 登录无效
                throw new LoginException(HttpEnum.TOKEN_INVALID.getCode(), HttpEnum.TOKEN_INVALID.getMessage());
            }
            // 用户校验,获取用户信息
            QueryWrapper<SystemAccountEntry> queryWrapper = new QueryWrapper<SystemAccountEntry>().eq("id", Integer.parseInt(loginId.toString()));
            SystemAccountEntry user = userMapper.selectOne(queryWrapper);
            if (user == null) {
                throw new LoginException(HttpEnum.TOKEN_INVALID.getCode(), HttpEnum.TOKEN_INVALID.getMessage());
            }
            // 判断用户是否被禁用
            if (user.getUserState() == 1) {
                throw new LoginException(HttpEnum.TOKEN_INVALID.getCode(), HttpEnum.TOKEN_INVALID.getMessage());
            }
            // 超级管理员不需要进行权限校验
            if (user.getId() != 1) {
                this.checkPerms(method, requestUri);
            }
        }

    }

    /**
     * 校验用户权限
     * @param method 请求方法
     * @param requestUri 请求地址
     * */
    public void checkPerms(Method method, String requestUri) {
        for (int i = 0; i <= 0; i++) {
            // 免权限校验
            if (method != null && method.isAnnotationPresent(NotPower.class)) {
                break;
            }
            // 将路由转换为权限
            String url = requestUri.replaceFirst("/bicycle-api/api/", "");
            String perms = url.replace("/", ":");
            // 权限校验
            StpUtil.checkPermission(perms);
        }

    }

}
