package com.medical.entry.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_user")
public class SystemUserEntry implements Serializable {

    @TableId("id")
    private Long id; // 用户id

    @TableField("username")
    private String username; // 用户名

    @TableField("password")
    private String password; // 登录密码

    @TableField("avatar")
    private String avatar; // 头像

    @TableField("email")
    private String email; // 邮箱

    @TableField("account")
    private String account; // 登录账号

    @TableField("user_state")
    private Integer userState; // 用户状态

    @TableField("remark")
    private String remark; // 用户备注

    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss", timezone = "GMT + 8")
    @DateTimeFormat(pattern = "yyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime; // 创建时间

    @JsonFormat(pattern = "yyy-MM-dd HH:mm:ss", timezone = "GMT + 8")
    @DateTimeFormat(pattern = "yyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private Date updateTime; // 更新时间

    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private String roleName;
}
