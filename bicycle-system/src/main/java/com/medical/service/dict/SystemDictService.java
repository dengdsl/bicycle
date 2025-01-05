package com.medical.service.dict;

import com.medical.utils.AjaxResult;
import com.medical.validate.dict.*;
import com.medical.validate.page.PageValidate;
import org.springframework.validation.annotation.Validated;

public interface SystemDictService {

    /**
     * 获取字典列表
     * */
    AjaxResult<Object> dictList(PageValidate pageValidate, SystemDictSearchValidate searchValidate);

    /**
     * 查询所有字典类型
     * */
    AjaxResult<Object> allDictList();

    /**
     * 新增字典
     * */
    AjaxResult<Object> addDict(SystemDictCreateValidate createValidate);

    /**
     * 获取详情数据
     * */
    AjaxResult<Object> getDictDetail(String id);

    /**
     * 编辑字典
     * */
    AjaxResult<Object> editDict(SystemDictUpdateValidate updateValidate);

    /**
     * 删除字典
     * */
    AjaxResult<Object> deleteDict(String id);

    /**
     * 获取字典数据列表
     * */
    AjaxResult<Object> dictDataList(PageValidate pageValidate, SystemDictDataSearchValidate searchValidate);

    /**
     * 获取所有字典数据
     * */
    AjaxResult<Object> all(String dictType);
    /**
     * 新增字典数据
     * */
    AjaxResult<Object> addDictData(SystemDictDataCreateValidate createValidate);

    /**
     * 更新字典数据
     * */
    AjaxResult<Object> editDictData(SystemDictDataUpdateValidate updateValidate);

    /**
     * 删除字典数据
     * */
    AjaxResult<Object> deleteDictData(String id);

    /**
     * 禁用/启用字典数据
     * */
    AjaxResult<Object> getDictDataDetail(String id);

}
