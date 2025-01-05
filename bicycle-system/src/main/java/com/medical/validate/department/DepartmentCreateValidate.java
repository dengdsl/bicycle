package com.medical.validate.department;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class DepartmentCreateValidate implements Serializable {

    @NotNull(message = "上级部门必填")
    private Long parentId;

    @NotNull(message = "部门名称必填")
    private String deptName;

    @NotNull(message = "部门负责人必填")
    private String duty;

    @NotNull(message = "部门负责人手机号必填")
    private String mobile;

    @NotNull(message = "排序参数必填")
    private Integer sort;

    @NotNull(message = "部门状态参数必填")
    private Integer isStop;
}
