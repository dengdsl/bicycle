package com.bicycle.controller.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bicycle.annotation.NotLogin;
import com.bicycle.annotation.NotPower;
import com.bicycle.entry.system.SystemConfigEntry;
import com.bicycle.mapper.account.SystemConfigMapper;
import com.bicycle.utils.AjaxResult;
import com.bicycle.utils.RedisUtil;
import com.bicycle.validate.config.SystemConfigUpdateValidate;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("api/config")

public class SystemConfigController {

    @Resource
    private SystemConfigMapper systemConfigMapper;
    /**
     * 获取所有系统配置信息
     * */
    @GetMapping("list")
    public AjaxResult<Object> getConfigList() {
        QueryWrapper<SystemConfigEntry> queryWrapper = new QueryWrapper<>();
        List<SystemConfigEntry> systemConfigEntries = systemConfigMapper.selectList(queryWrapper);
        return AjaxResult.success(systemConfigEntries);
    }

    /**
     * 获取单个配置信息
     * */
    @NotPower
    @NotLogin
    @PostMapping("get")
    public AjaxResult<Object> getConfig(@Validated @RequestBody ArrayList<String> names) {
        QueryWrapper<SystemConfigEntry> queryWrapper = new QueryWrapper();
        queryWrapper.in("name", names);
        List<SystemConfigEntry> systemConfigEntries = systemConfigMapper.selectList(queryWrapper);
        Map<String, String> config = new HashMap();
        for (SystemConfigEntry entry : systemConfigEntries) {
            config.put(entry.getName(), entry.getValue());
        }
        return AjaxResult.success(config);
    }

    /**
     * 保存配置信息
     * */
    @PostMapping("save")
    public AjaxResult<Object> saveConfig(@Validated @RequestBody ArrayList<SystemConfigUpdateValidate> configList) {
        // 先根据传递的id查询配置信息
        QueryWrapper<SystemConfigEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", configList.stream().map(SystemConfigUpdateValidate::getId).toList());
        List<SystemConfigEntry> systemConfigEntries = systemConfigMapper.selectList(queryWrapper);
        // 遍历查询出来的配置信息，再根据传递的id进行匹配后修改value值
        for (SystemConfigEntry systemConfigEntry : systemConfigEntries) {
            for (SystemConfigUpdateValidate config : configList) {
                if (systemConfigEntry.getId().equals(config.getId())) {
                    systemConfigEntry.setValue(config.getValue());
                    systemConfigEntry.setUpdateTime(new Date());
                    break;
                }
            }
        }
        systemConfigMapper.updateBatchById(systemConfigEntries);
        RedisUtil.setWithExpiration("configChange", "hasChange", 1000 * 60 * 60 * 24L);
        return AjaxResult.success();
    }
}
