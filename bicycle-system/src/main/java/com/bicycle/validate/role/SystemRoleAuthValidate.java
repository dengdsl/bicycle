package com.bicycle.validate.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class SystemRoleAuthValidate implements Serializable {

    @NotNull(message = "角色id不能为空")
    private Long id;

    @NotBlank(message = "权限ID参数必填")
    private String menuIds;
}
