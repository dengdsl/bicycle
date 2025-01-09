package com.bicycle.validate.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class SettingUserValidate implements Serializable {

    @NotNull(message = "请上传头像")
    private String avatar;

    @NotNull(message = "账号必填")
    private String account;

    private String username;

    private String currPassword;

    private String password;

    private String passwordConfirm;
}
