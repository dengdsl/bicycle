package com.bicycle.entry.dict;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 字典数据表是类
 * */
@Data
@TableName("sys_dict_data")
public class SystemDictDataEntry implements Serializable {

    @TableId(value = "id")
    private Long id;  // 主键

    @TableField("dict_type")
    private String dictType; // 字典类型

    @TableField("name")
    private String name; // 名称

    @TableField("value")
    private String value; // 值

    @TableField("state")
    private Integer state; // 状态：0-禁用，1-启用

    @TableField("remark")
    private String remark; // 备注

    @TableField("sort")
    private Integer sort; // 排序
}
