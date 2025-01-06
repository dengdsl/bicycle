package com.bicycle.entry.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统配置表实体类
 * */
@Data
@TableName("sys_config")
public class SystemConfigEntry implements Serializable {

    @TableId(type = IdType.AUTO)
    public Integer id;

    @TableField("type")
    private String type;

    @TableField("name")
    private String name;

    @TableField("value")
    private String value;

    //@JsonFormat(pattern = "yyy-MM-dd HH:mm:ss", timezone = "GMT + 8")
    //@DateTimeFormat(pattern = "yyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    //@JsonFormat(pattern = "yyy-MM-dd HH:mm:ss", timezone = "GMT + 8")
    //@DateTimeFormat(pattern = "yyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private Date updateTime;
}
