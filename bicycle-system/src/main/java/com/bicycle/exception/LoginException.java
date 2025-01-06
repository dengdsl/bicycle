package com.bicycle.exception;

/**
 * 自动逸登录异常类
 * */
public class LoginException extends CustomException{


    public LoginException(Integer code, String message) {
        super(code, message);
    }
}
