package com.bicycle.entry.role;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_role")
public class SystemRoleEntry implements Serializable {

    @TableId(value = "id")
    private Long id;  // 主键

    @TableField("role_name")
    private String roleName; // 角色名称

    @TableField("sort")
    private Long sort; // 排序

    @TableField("role_count")
    private Long roleCount; // 管理员人数

    @TableField("role_state")
    private Integer roleState; // 创建人

    @TableField("remark")
    private String remark; // 备注

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT + 8")
    @TableField("create_time")
    private Date createTime; // 创建时间

}
