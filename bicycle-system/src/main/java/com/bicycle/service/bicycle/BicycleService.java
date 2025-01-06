package com.bicycle.service.bicycle;

import com.bicycle.utils.AjaxResult;
import com.bicycle.validate.bicycle.BicycleCreateValidate;
import com.bicycle.validate.bicycle.BicycleUpdateValidate;
import com.bicycle.validate.page.PageValidate;
import com.bicycle.validate.bicycle.BicycleSearchValidate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BicycleService {

    /**
     * 获取自行车列表
     * */
     AjaxResult<Object> getBicycleList(PageValidate pageValidate, BicycleSearchValidate searchValidate);

    /**
     * 新增自行车信息
     * */
     AjaxResult<Object> addBicycle(BicycleCreateValidate createValidate);

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
     AjaxResult<Object> importBicycle(MultipartFile file) throws IOException;

     /**
      * 批量导出自行车信息
      * */
     AjaxResult<Object> exportBicycle( );
}
