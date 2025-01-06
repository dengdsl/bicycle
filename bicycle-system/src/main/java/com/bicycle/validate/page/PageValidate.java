package com.bicycle.validate.page;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.io.Serializable;

@Data
public class PageValidate implements Serializable {

    // 当前分页
    @DecimalMin(value = "1", message = "pageNo参数必须大于0的数字")
    public Integer pageNo = 1;

    // 每页条数
    @DecimalMin(value = "1", message = "pageSize参数必须是大于0的数字")
    @DecimalMax(value = "60", message = "pageSize参数必须是小于60的数字")
    private Integer pageSize = 15;
}
