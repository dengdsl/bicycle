package com.bicycle.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Convert {

    // 转换为 String
    public static String toStr(Object obj) {
        if (obj == null) {
            return "";
        }
        return obj.toString();
    }

    // 转换为 Integer
    public static Integer toInt(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return Integer.parseInt(toStr(obj));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // 转换为 Long
    public static Long toLong(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return Long.parseLong(toStr(obj));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // 转换为 Double
    public static Double toDouble(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return Double.parseDouble(toStr(obj));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // 转换为 Float
    public static Float toFloat(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return Float.parseFloat(toStr(obj));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // 转换为 BigDecimal
    public static BigDecimal toBigDecimal(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return new BigDecimal(toStr(obj));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // 转换为 Boolean
    public static Boolean toBool(Object obj, Boolean defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        try {
            return Boolean.parseBoolean(toStr(obj));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    // 解析日期字符串到 Date 对象
    public static Date parseDateToStr(String format, Object obj) {
        if (obj == null || format == null) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(toStr(obj));
        } catch (ParseException e) {
            return null;
        }
    }
}

