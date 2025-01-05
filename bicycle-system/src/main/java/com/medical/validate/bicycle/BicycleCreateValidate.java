package com.medical.validate.bicycle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class BicycleCreateValidate implements Serializable {

    @NotNull(message = "名称必填")
    @NotBlank(message = "名称必填")
    private String title;

    @NotNull(message = "图片参数必填")
    @NotBlank(message = "图片参数必填")
    private String image;

    private String remark;
}
