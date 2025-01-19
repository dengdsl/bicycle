package com.bicycle.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFPictureData;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

@Slf4j
public class ReflectionUtils {

    /**
     * 通过反射调用指定对象的 setter 方法设置属性值
     *
     * @param entity       目标对象
     * @param propertyName 属性名称
     * @param val          要设置的值
     * @throws RuntimeException 如果发生反射相关异常
     */
    public static void invokeSetter(Object entity, String propertyName, Object val) {
        if (entity == null || propertyName == null || propertyName.isEmpty()) {
            throw new IllegalArgumentException("参数 entity 和 propertyName 不能为空");
        }

        try {
            // 构造 setter 方法名称
            String setterMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);

            // 获取目标类
            Class<?> entityClass = entity.getClass();

            // 查找 setter 方法
            Method setterMethod = null;
            for (Method method : entityClass.getMethods()) {
                if (method.getName().equals(setterMethodName) && method.getParameterCount() == 1) {
                    setterMethod = method;
                    break;
                }
            }

            if (setterMethod == null) {
                throw new NoSuchMethodException("未找到属性 " + propertyName + " 的 setter 方法");
            }

            // 将值转换为 setter 方法参数类型
            Class<?> parameterType = setterMethod.getParameterTypes()[0];
            Object convertedValue = convertValue(val, parameterType);

            // 调用 setter 方法
            setterMethod.invoke(entity, convertedValue);
        } catch (Exception e) {
            log.error("调用setter方法失败：" + e.getMessage());
            throw new RuntimeException("调用 setter 方法失败", e);
        }
    }

    /**
     * 将值转换为目标类型
     *
     * @param value      原始值
     * @param targetType 目标类型
     * @return 转换后的值
     */
    private static Object convertValue(Object value, Class<?> targetType) {
        if (value == null) {
            return null;
        }

        if (targetType.isInstance(value)) {
            return value;
        }

        if (targetType == String.class) {
            return value.toString();
        } else if (targetType == int.class || targetType == Integer.class) {
            return Integer.parseInt(value.toString());
        } else if (targetType == long.class || targetType == Long.class) {
            return Long.parseLong(value.toString());
        } else if (targetType == double.class || targetType == Double.class) {
            return Double.parseDouble(value.toString());
        } else if (targetType == float.class || targetType == Float.class) {
            return Float.parseFloat(value.toString());
        } else if (targetType == boolean.class || targetType == Boolean.class) {
            return Boolean.parseBoolean(value.toString());
        } else if (targetType == char.class || targetType == Character.class) {
            return value.toString().charAt(0);
        } else if (targetType == java.util.Date.class) {
            // 根据需要调整日期格式解析
            try {
                String date = value.toString();
                if (date.matches("\\d{1,2}-\\d{1,2}月-\\d{4}")) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", java.util.Locale.CHINA);
                    return sdf.parse(date);
                }
                return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(value.toString());
            } catch (Exception e) {
                throw new IllegalArgumentException("无法将值转换为 java.util.Date 类型: " + value);
            }
        } else if (targetType == XSSFPictureData.class) {
            return (XSSFPictureData) value;
        }
        throw new IllegalArgumentException("不支持的目标类型: " + targetType.getName());
    }
}

