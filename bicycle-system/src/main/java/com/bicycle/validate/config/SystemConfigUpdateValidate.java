package com.bicycle.validate.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SystemConfigUpdateValidate {

    @NotNull(message = "id缺失")
    public Integer id;

    @NotNull(message = "value缺失")
    @NotBlank(message = "value不能为空")
    private String value;


}
