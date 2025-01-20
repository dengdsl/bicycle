package com.bicycle.utils;

import java.util.Calendar;

public class FilePathUtil {

    /**
     * 创建文件夹
     * */
    public static String generateBasePath(String path) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 月份从0开始，需加1
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 补零处理
        String formattedMonth = (month < 10 ? "0" : "") + month;
        String formattedDay = (day < 10 ? "0" : "") + day;

        return String.format("%s/%d/%s/%s", path, year, formattedMonth, formattedDay);
    }
}
