package com.bicycle.service.account;


import com.bicycle.utils.AjaxResult;


/**
 * 用户登录相关接口
 * */
public interface IAccountService {

    /**
     * 获取图片验证码
     * */
    AjaxResult<Object> captcha();

    /**
     * 用户登录
     * */
    AjaxResult<Object> login(String loginValidate);

    /**
     * 退出登录
     * */
    AjaxResult<Object> logout(String token);

    /**
     * 获取应用配置信息
     * */
    AjaxResult<Object> config();

    /**
     * 获取用户信息与用户权限列表
     * */
    AjaxResult<Object> self(Long adminId);

    /**
     * 获取路由菜单
     * */
    AjaxResult<Object> routes(Long adminId);
}
