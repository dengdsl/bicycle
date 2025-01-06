package com.bicycle.mapper.account;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bicycle.entry.system.SystemConfigEntry;
import org.apache.ibatis.annotations.Select;

public interface SystemConfigMapper extends BaseMapper<SystemConfigEntry> {

    /**
     * 根据名称查询配置属性
     * */
    @Select("select * from sys_config where name=#{name}")
    SystemConfigEntry getConfigByName(String name);
}
