package com.bicycle.validate.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class SystemUserSearchValidate implements Serializable {

    private String account; // 用户账号

    private String nickname; // 用户昵称

    private Long roleId; // 角色id
}
