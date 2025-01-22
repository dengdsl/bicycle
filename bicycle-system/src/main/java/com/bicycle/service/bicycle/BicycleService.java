package com.bicycle.service.bicycle;

import com.bicycle.utils.AjaxResult;
import com.bicycle.validate.bicycle.BicycleCreateValidate;
import com.bicycle.validate.bicycle.BicycleUpdateValidate;
import com.bicycle.validate.page.PageValidate;
import com.bicycle.validate.bicycle.BicycleSearchValidate;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.DecoderException;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface BicycleService {

    /**
     * 获取自行车列表
     * */
     AjaxResult<Object> getBicycleList(PageValidate pageValidate, BicycleSearchValidate searchValidate);

    /**
     * 新增自行车信息
     * */
     AjaxResult<Object> addBicycle(BicycleCreateValidate createValidate, HttpServletRequest request);

    /**
     * 编辑自行车信息
     * */
     AjaxResult<Object> editBicycle(BicycleUpdateValidate updateValidate );

    /**
     * 删除自行车信息
     * */
     AjaxResult<Object> deleteBicycle(String id );

     /**
      * 获取自行车详情数据
      * */
     AjaxResult<Object> getBicycleDetail(String id);

    /**
     * 批量导入自行车信息
     * */
     AjaxResult<Object> importBicycle(MultipartFile file, HttpServletRequest request) throws IOException;

     /**
      * 批量导出自行车信息
      * */
     void exportBicycle(HttpServletResponse response, List<String> ids) throws IOException;

    /**
     * 扫描二维码进行查询
     * */
    AjaxResult<Object> queryByQrcode(String qrcode);

    /**
     * 下载文件导入模版
     * */
    void downloadImportTemplate(HttpServletResponse response) throws IOException, DecoderException;
}
