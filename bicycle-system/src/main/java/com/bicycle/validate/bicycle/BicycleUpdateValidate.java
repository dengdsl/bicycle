package com.bicycle.validate.bicycle;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BicycleUpdateValidate implements Serializable {

    @NotNull(message = "缺少ID编号")
    @NotBlank(message = "缺少ID编号")
    private String id;

    @NotNull(message = "请选择型号")
    @NotBlank(message = "请选择型号")
    private Integer model;

    @NotNull(message = "请输入车架号")
    @NotBlank(message = "请输入车架号")
    private String frameNo;

    @NotNull(message = "请选择生产日期")
    @NotBlank(message = "请选择生产日期")
    private Date produceTime;

    @NotNull(message = "图片参数必填")
    @NotBlank(message = "图片参数必填")
    private String image;

    private String remark;

    private Integer conclusion;
}
