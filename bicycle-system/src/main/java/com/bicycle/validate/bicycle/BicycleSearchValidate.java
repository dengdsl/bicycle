package com.bicycle.validate.bicycle;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
public class BicycleSearchValidate implements Serializable {

    // id
    private String id;

    private String proName; // 产品名称

    // 型号
    private Integer model;

    // 车架号
    private String frameNo;

    // 结论
    private String conclusion;

    // 空孔
    private String hollowHole;
    // 内折
    private String inFold;
    // 乱纱
    private String raveling;

    // 生产日期
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT + 8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String produceTimeStart;

    // 生产日期
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT + 8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String produceTimeEnd;
}
