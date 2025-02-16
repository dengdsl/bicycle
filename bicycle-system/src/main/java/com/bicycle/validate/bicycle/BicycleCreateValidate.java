package com.bicycle.validate.bicycle;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class BicycleCreateValidate implements Serializable {

    @NotNull(message = "请选择产品名称")
    private String proName; // 产品名称

    @NotNull(message = "请选择型号")
    private String model;

    @NotNull(message = "请输入产品编号")
    @NotBlank(message = "请输入产品编号")
    private String frameNo;

    @NotNull(message = "请选择生产日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date produceTime;

    @NotNull(message = "图片参数必填")
    @NotBlank(message = "图片参数必填")
    private String image;

    // 空孔
    private String hollowHole;
    // 内折
    private String inFold;
    // 乱纱
    private String raveling;


    private String remark;

    private String conclusion;

}
