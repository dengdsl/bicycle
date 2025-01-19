package com.bicycle.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Excel {

    /**
     * 导入到excel中的名字
     * */
    String name() default "";

    /**
     * 日期格式
     * */
    String dateFormat() default "yyyy-MM-dd";

    /**
     * 是否读取图片
     */
    boolean getPicture() default false;

    /**
     * 当值为空时，字段的默认值
     * */
    String defaultValue() default "";

}
