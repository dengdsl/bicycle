package com.medical.validate.dict;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class SystemDictDataSearchValidate implements Serializable {

    @NotNull(message = "字典类型必填")
    private String dictType; // 字典类型

    private String name; // 数据名称

    private Integer state; // 数据状态

    private String value; // 数据值

    private String remark; // 数据备注

    private Integer sort; // 排序
}
