package com.bicycle.validate.menus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


@Data
public class UpdateMenuValidate implements Serializable {

    @NotNull(message = "id参数必传且需大于0")
    private Integer id;

    @NotNull(message = "上级菜单不能为空")
    @DecimalMin(value = "0", message = "上级菜单值不能少于0")
    private Integer pid;

    @NotNull(message = "menuType参数缺失")
    private String menuType;

    @NotNull(message = "缺少参数menuName")
    @Length(min = 1, max = 30, message = "菜单名称必须在1~30个字符内")
    private String menuName;

    @Length(max = 100, message = "图标名称不能超过100个字符")
    private String menuIcon;

    @NotNull(message = "排序号不能为空")
    private Integer menuSort;

    @Length(max = 100, message = "权限字符不能超过100个字符")
    private String perms;

    @Length(max = 200, message = "路由地址不能超过200个字符")
    private String paths;

    @Length(max = 200, message = "前端组件不能超过200个字符")
    private String component;

    @Length(max = 200, message = "选中菜单路径不能超过200个字符")
    private String selected;

    @Length(max = 200, message = "路由参数不能超过200个字符")
    private String params;

    @NotNull(message = "请选择缓存状态")
    private Integer isCache;

    @NotNull(message = "请选择显示状态")
    private Integer isShow;

    @NotNull(message = "请选择菜单状态")
    private Integer isDisable;


}
