package com.bicycle.entry.bicycle;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 供应商表实体类
 * */
@Data
@TableName("bicycle_info")
public class BicycleEntry implements Serializable {

    @TableId(value = "id")
    private String id; // id

    @TableField("model")
    private Integer model; // 型号

    @TableField("frame_no")
    private String frameNo; // 车架号

    @TableField("image")
    private String image; // X光图片

    @TableField("qrcode")
    private String qrcode; // 二维码编码

    @TableField("qr_img")
    private String qrImg; // 二维码图片

    @TableField("conclusion")
    private Integer conclusion; // 结论

    @TableField("remark")
    private String remark; // 备注

    @TableField("is_del")
    private Integer isDel; // 是否删除

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT + 8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField("produce_time")
    private Date produceTime; // 创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT + 8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime; // 创建时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT + 8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private Date updateTime; // 更新时间

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT + 8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("delete_time")
    private Date deleteTime; // 删除时间
}

