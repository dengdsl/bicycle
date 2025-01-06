package com.bicycle.vo.account;

import lombok.Data;

import java.io.Serializable;

@Data
public class SystemLoginVo implements Serializable {

    /**token信息**/
    private String token;

    /**用户id**/
    private Long id;
}
