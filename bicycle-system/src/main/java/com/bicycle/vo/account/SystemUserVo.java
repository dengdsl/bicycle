package com.bicycle.vo.account;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class SystemUserVo implements Serializable {

    /**用户id**/
    private Long id;

    /**登录账号**/
    private String account;

    /**用户邮箱**/
    private String email;

    /**用户登录密码**/
    private String password;

    /**用户名称**/
    private String username;

    /**用户名称**/
    private String avatar;

    /**用户状态**/
    private Integer userState;

    /**备注**/
    private String remark;

    /**创建时间**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**更新时间**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
