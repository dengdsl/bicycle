package com.medical.service.bicycle;

import com.medical.utils.AjaxResult;
import com.medical.validate.bicycle.BicycleCreateValidate;
import com.medical.validate.bicycle.BicycleUpdateValidate;
import com.medical.validate.page.PageValidate;
import com.medical.validate.bicycle.BicycleSearchValidate;

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
     AjaxResult<Object> importBicycle( );

     /**
      * 批量导出自行车信息
      * */
     AjaxResult<Object> exportBicycle( );
}
