package com.bicycle.mapper.account;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bicycle.entry.system.SystemConfigEntry;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface SystemConfigMapper extends BaseMapper<SystemConfigEntry> {

    /**
     * 根据名称查询配置属性
     * */
    @Select("select * from sys_config where name=#{name}")
    SystemConfigEntry getConfigByName(String name);

    /*
    * 批量跟新配置信息
    * */
    @Update({
            "<script>",
            "UPDATE sys_config",
            "<trim prefix='SET' suffixOverrides=','>",
            "<foreach collection='configList' item='config' separator=','>",
            "value = CASE id",
            "<foreach collection='configList' item='item'>",
            "WHEN #{item.id} THEN #{item.value}",
            "</foreach>",
            "END",
            "</foreach>",
            "</trim>",
            "WHERE id IN",
            "<foreach collection='configList' item='config' open='(' separator=',' close=')'>",
            "#{config.id}",
            "</foreach>",
            "</script>"
    })
    int updateBatchById(List<SystemConfigEntry> configList);
}
