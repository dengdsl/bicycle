package com.medical.utils;


import com.medical.enums.HttpEnum;
import lombok.Data;

import java.util.ArrayList;

/**
 * ajax请求相应工具类
 * */
@Data
public class AjaxResult <T>{

    /**状态码**/
    private Integer code;
    /**消息**/
    private String message;
    /**相应数据**/
    private T data;

    /** 无参构造 **/
    protected AjaxResult() {}

    /**
     * 有参构造方法
     * * @author dsl
     * @param code 状态码
     * @param message 提示信息
     * */
    public AjaxResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 有参构造方法
     * * @author dsl
     * @param code 状态码
     * @param message 提示信息
     * @param data 响应数据
     * */
    public AjaxResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 请求成功
     * */
    public static AjaxResult<Object> success(){
        return new AjaxResult<>(HttpEnum.SUCCESS.getCode(), HttpEnum.SUCCESS.getMessage(), new ArrayList<>());
    }

    /**
     * 请求成功
     * @param code 自定义状态码
     * */
    public static AjaxResult<Object> success(Integer code) {
        return new AjaxResult<>(code, HttpEnum.SUCCESS.getMessage(), new ArrayList<>());
    }

    /**
     * 请求成功
     * @param message 自定义提示信息
     * */
    public static AjaxResult<Object> success(String message) {
        return new AjaxResult<>(HttpEnum.SUCCESS.getCode(), message, new ArrayList<>());
    }

    /**
     * 请求成功
     * @param data 自定义响应数据
     * */
    public static <T> AjaxResult<Object> success(T data) {
        return new AjaxResult<>(HttpEnum.SUCCESS.getCode(), HttpEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 请求成功
     * @param code 自定义状态码
     * @param message 自定义提示信息
     * */
    public static AjaxResult<Object> success(Integer code, String message) {
        return new AjaxResult<>(code, message, new ArrayList<>());
    }

    /**
     * 请求成功
     * @param code 自定义状态码
     * @param data 自定义响应数据
     * */
    public static <T> AjaxResult<Object> success(Integer code, T data) {
        return new AjaxResult<>(code, HttpEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 请求成功
     * @param message 自定义提示信息
     * @param data 自定义响应信息
     * */
    public static <T> AjaxResult<Object> success(String message, T data) {
        return new AjaxResult<>(HttpEnum.SUCCESS.getCode(), message, data);
    }

    /**
     * 请求成功
     * @param code 自定义状态码
     * @param message 自定义提示信息
     * @param data 自定义相应数据
     * */
    public static <T> AjaxResult<Object> success(Integer code, String message, T data) {
        return new AjaxResult<>(code, message, data);
    }

    /**
     * 请求失败
     * */
    public static AjaxResult<Object> failed(){
        return new AjaxResult<>(HttpEnum.FAILED.getCode(), HttpEnum.FAILED.getMessage(), new ArrayList<>());
    }

    /**
     * 请求失败
     * @param code 自定义状态码
     * */
    public static AjaxResult<Object> failed(Integer code) {
        return new AjaxResult<>(code, HttpEnum.FAILED.getMessage(), new ArrayList<>());
    }

    /**
     * 请求失败
     * @param message 自定义提示信息
     * */
    public static AjaxResult<Object> failed(String message) {
        return new AjaxResult<>(HttpEnum.FAILED.getCode(), message, new ArrayList<>());
    }

    /**
     * 请求失败
     * @param data 自定义响应数据
     * */
    public static <T> AjaxResult<Object> failed(T data) {
        return new AjaxResult<>(HttpEnum.FAILED.getCode(), HttpEnum.FAILED.getMessage(), data);
    }

    /**
     * 请求失败
     * @param code 自定义状态码
     * @param message 自定义提示信息
     * */
    public static AjaxResult<Object> failed(Integer code, String message) {
        return new AjaxResult<>(code, message, new ArrayList<>());
    }

    /**
     * 请求失败
     * @param code 自定义状态码
     * @param data 自定义响应数据
     * */
    public static <T> AjaxResult<Object> failed(Integer code, T data) {
        return new AjaxResult<>(code, HttpEnum.FAILED.getMessage(), data);
    }

    /**
     * 请求失败
     * @param message 自定义提示信息
     * @param data 自定义响应信息
     * */
    public static <T> AjaxResult<Object> failed(String message, T data) {
        return new AjaxResult<>(HttpEnum.FAILED.getCode(), message, data);
    }

    /**
     * 请求失败
     * @param code 自定义状态码
     * @param message 自定义提示信息
     * @param data 自定义相应数据
     * */
    public static <T> AjaxResult<Object> failed(Integer code, String message, T data) {
        return new AjaxResult<>(code, message, data);
    }

}
