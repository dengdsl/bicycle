package com.bicycle.entry.menus;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_menu")
public class SystemMenusEntry implements Serializable {


    @TableId(value="id", type= IdType.AUTO)
    private Integer id;        // 主键

    @TableField("pid")
    private Integer pid;       // 上级菜单

    @TableField("menu_type")
    private String menuType;   // 权限类型: [M=目录, C=菜单, A=按钮]

    @TableField("menu_Name")
    private String menuName;   // 菜单名称

    @TableField("menu_icon")
    private String menuIcon;   // 菜单图标

    @TableField("menu_sort")
    private Integer menuSort;  // 菜单排序

    @TableField("perms")
    private String perms;      // 权限标识

    @TableField("paths")
    private String paths;      // 路由地址

    @TableField("component")
    private String component;  // 前端组件

    @TableField("selected")
    private String selected;   // 选中路径

    @TableField("params")
    private String params;     // 路由参数

    @TableField("is_cache")
    private Integer isCache;   // 是否缓存: [0=否, 1=是]

    @TableField("is_show")
    private Integer isShow;    // 是否显示: [0=否, 1=是]

    @TableField("is_disable")
    private Integer isDisable; // 是否禁用: [0=否, 1=是]

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT + 8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;  // 创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT + 8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private Date updateTime;  // 更新时间
}
