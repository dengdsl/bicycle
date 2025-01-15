package com.bicycle.validate.bicycle;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
public class BicycleSearchValidate implements Serializable {

    // id
    private String id;

    // 型号
    private Integer model;

    // 车架号
    private String frameNo;

    // 结论
    private String conclusion;

    // 生产日期
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT + 8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String produceTime;
}
