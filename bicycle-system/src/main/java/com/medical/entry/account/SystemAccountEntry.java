package com.medical.entry.account;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表实体
 * */
@Data
@TableName("sys_user")
public class SystemAccountEntry implements Serializable {

    /**用户id**/
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**登录账号**/
    @TableField("account")
    private String account;

    /**用户邮箱**/
    @TableField("email")
    private String email;

    /**用户登录密码**/
    @TableField("password")
    private String password;

    /**用户名称**/
    @TableField("username")
    private String username;

    /**用户头像**/
    @TableField("avatar")
    private String avatar;

    /**用户状态**/
    @TableField("user_state")
    private Integer userState;

    /**备注**/
    @TableField("remark")
    private String remark;

    /**创建时间**/
    @TableField("create_time")
    private Date createTime;

    /**创建时间**/
    @TableField("update_time")
    private Date updateTime;


}
