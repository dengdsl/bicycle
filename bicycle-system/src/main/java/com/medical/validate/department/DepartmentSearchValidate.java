package com.medical.validate.department;

import lombok.Data;

import java.io.Serializable;

@Data
public class DepartmentSearchValidate implements Serializable {

    private String deptName;

    private Integer deptState;
}
