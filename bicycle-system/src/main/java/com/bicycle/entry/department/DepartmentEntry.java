package com.bicycle.entry.department;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_dept")
public class DepartmentEntry implements Serializable {

    @TableId("id")
    private Long id;

    @TableField("parent_id")
    private Long parentId;

    @TableField("dept_name")
    private String deptName;

    @TableField("duty")
    private String duty;

    @TableField("mobile")
    private String mobile;

    @TableField("sort")
    private Integer sort;

    @TableField("is_stop")
    private Integer isStop;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private Date updateTime;
}
