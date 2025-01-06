package com.bicycle.vo.account;

import lombok.Data;
import java.io.Serializable;

@Data
public class SystemAdminAuthVo implements Serializable {

    private Object user;            // 用户信息
    private Object permissions;     // 权限集合：[*] => 所有权限，['user:add'] => 部分权限
}
