package com.bicycle.validate.dict;

import lombok.Data;

import java.io.Serializable;

@Data
public class SystemDictSearchValidate implements Serializable {

    private String dictType;    // 字典类型

    private String dictName;    // 字典名称

    private Integer dictStatus; // 数据状态
}
