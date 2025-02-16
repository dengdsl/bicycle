package com.bicycle.vo.bicycle;

import com.bicycle.annotation.Excel;
import lombok.Data;
import org.apache.poi.xssf.usermodel.XSSFPictureData;

import java.util.Date;
import java.util.List;

@Data
public class ExcelRowDetailVo {

    @Excel(name = "产品名称")
    private String proName;

    @Excel(name = "产品型号")
    private String model;

    @Excel(name = "产品编号")
    private String frameNo;

    @Excel(name = "X光图片", getPicture = true)
    private List<XSSFPictureData> images;

    @Excel(name = "生产日期")
    private Date produceTime;

    @Excel(name = "结论")
    private String conclusion;

    @Excel(name = "空孔")
    private String hollowHole;

    @Excel(name = "内折")
    private String inFold;

    @Excel(name = "乱纱")
    private String raveling;

    @Excel(name = "备注")
    private String remark;
}
