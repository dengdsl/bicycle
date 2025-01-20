package com.bicycle.utils;

import com.bicycle.entry.system.SystemConfigEntry;
import com.bicycle.mapper.account.SystemConfigMapper;
import org.springframework.stereotype.Component;

/**
 * 获取文件上传的服务器地址和路径
 */
@Component
public class ConfigUtils {

    private static String serverUrl;

    private static String filePath;
    private static SystemConfigMapper configMapper;

    public ConfigUtils(SystemConfigMapper systemConfigMapper) {
        configMapper = systemConfigMapper;
        updateConfig();
    }

    public static String getServerUrl() {
        Object configChange = RedisUtil.get("configChange");
        if (serverUrl == null || configChange != null) {
            updateConfig();
        }
        return serverUrl;
    }

    public static String getFilePath() {
        Object configChange = RedisUtil.get("configChange");
        if (filePath == null || configChange != null) {
            updateConfig();
        }
        return filePath;
    }

    // 更新服务器地址和文件路径
    public static void updateConfig() {
        // 初始化文件保存文件夹路径
        SystemConfigEntry qrcodeFilePath = configMapper.getConfigByName("filePath");
        if (qrcodeFilePath != null && !qrcodeFilePath.getValue().isEmpty()) {
            filePath = qrcodeFilePath.getValue();
        }
        // 初始化文件保存服务器路径
        SystemConfigEntry serverUrlEntry = configMapper.getConfigByName("serverUrl");
        if (serverUrlEntry != null) {
            // 如果地址后未拼接斜杠，自动补充斜杠
            if (serverUrlEntry.getValue().endsWith("/")) {
                serverUrl = serverUrlEntry.getValue() + "static";
            } else {
                serverUrl = serverUrlEntry.getValue() + "/static";
            }
        }
        try {
            Object configChange = RedisUtil.get("configChange");
            if (configChange != null) {
                RedisUtil.delete("configChange");
            }
        } catch (Exception e) {}
    }
}
