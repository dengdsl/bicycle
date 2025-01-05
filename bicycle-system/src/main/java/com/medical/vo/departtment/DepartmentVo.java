package com.medical.vo.departtment;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DepartmentVo implements Serializable {

    private Long id;

    private Long parentId;

    private String deptName;

    private String duty;

    private String mobile;

    private Integer sort;

    private Integer isStop;

    private String updateTime;
}
