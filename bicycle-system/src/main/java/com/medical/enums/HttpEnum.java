package com.medical.enums;

/**
 * http请求相应状态码等等信息
 * */
public enum HttpEnum {

    /**相应成功**/
    SUCCESS(200, "成功"),
    /**失败结果**/
    FAILED(300, "失败"),
    /**token参数无效**/
    TOKEN_INVALID(301, "token参数无效"),
    /**表单参数验证异常**/
    PARAMS_VALID_ERROR(310, "参数校验错误"),
    /**参数类型异常**/
    PARAMS_TYPE_ERROR(311, "参数类型错误"),
    /**登录账号被禁用**/
    ACCOUNT_DISABLE_ERROR(331, "登录账号已被禁用了"),
    /**账号错误**/
    ACCOUNT_LOGIN_ERROR(331, "登录账号错误"),
    /**密码错误**/
    PASSWORD_LOGIN_ERROR(331, "登录密码错误"),
    /**请求方法错误**/
    REQUEST_METHOD_ERROR(312, "请求方法错误"),
    /**mybatis-plus异常**/
    ASSERT_MYBATIS_ERROR(314, "断言Mybatis错误"),
    /**验证码错误**/
    CAPTCHA_ERROR(334, "验证码错误"),
    /**没有权限**/
    NO_PERMISSION(403, "无相关权限"),
    /**请求接口不存在**/
    REQUEST_404_ERROR(404, "请求接口不存在"),
    /**系统错误**/
    SYSTEM_ERROR(500, "系统错误"),
    ;


    /**状态码**/
    private final Integer code;
    /**提示信息**/
    private final String message;

    /**构造方法**/
    HttpEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取状态码
     * */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取提示信息
     * */
    public String getMessage() {
        return message;
    }
}
