package com.bicycle.vo.bicycle;

import com.bicycle.annotation.Excel;
import lombok.Data;
import org.apache.poi.xssf.usermodel.XSSFPictureData;

import java.util.Date;

@Data
public class ExcelRowDetailVo {

    @Excel(name = "型号")
    private String model;

    @Excel(name = "车架号")
    private String frameNo;

    @Excel(name = "X光图片", getPicture = true)
    private XSSFPictureData images;

    @Excel(name = "生产日期")
    private Date produceTime;

    @Excel(name = "结论")
    private String conclusion;

    @Excel(name = "备注")
    private String remark;
}
