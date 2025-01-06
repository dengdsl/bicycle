package com.bicycle.validate.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class SystemRoleCreateValidate implements Serializable {

    @NotBlank(message = "角色名称不为空")
    private String roleName;

    @NotNull(message = "排序参数必填")
    private Long sort;

    @NotNull(message = "管理员状态参数必填")
    private Integer roleState;

    private String remark;
}
