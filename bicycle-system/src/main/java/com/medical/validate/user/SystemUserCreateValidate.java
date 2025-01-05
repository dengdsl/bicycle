package com.medical.validate.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SystemUserCreateValidate implements Serializable {

    @NotBlank(message = "请输入账号")
    private String account;

    @NotBlank(message = "请输入用户名称")
    private String nickname;

    @NotNull(message = "请选择所属部门")
    private List<Long> deptIds;

    @NotNull(message = "请选择所属角色")
    private List<Long> roleIds;

    @NotBlank(message = "请上传用户头像")
    private String avatar;

    @NotBlank(message = "请输入密码")
    private String password;

    @NotBlank(message = "请再次输入密码")
    private String passwordConfirm;

    @NotNull(message = "管理员状态必填")
    private Integer userState;

    private String email;

    private String remark;
}
