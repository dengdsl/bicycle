package com.medical.annotation;

import java.lang.annotation.*;

/**
 * 该自定义注解用于表明不需要登录验证,只需要在方法上面使用该注解即可
 * 该注解无需要实现类,被全局的权限校验拦截器监听
 * */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotPower {
}
