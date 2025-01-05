package com.medical.exception;


import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常
 *
 * @Data：自动生成类属性的构造器
 * @EqualsAndHashCode：自动生成toString方法和equals方法
 * callSuper = true：表示在生成方法的同时调用父类中的方法
 * */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomException extends RuntimeException{

    private Integer code;
    private String message;

    public CustomException(){}

    public CustomException(Integer code, String message){
        this.code = code;
        this.message = message;
    }
}
