package com.medical.controller.dictcontroller;

import com.medical.annotation.NotPower;
import com.medical.service.dict.SystemDictService;
import com.medical.utils.AjaxResult;
import com.medical.validate.dict.*;
import com.medical.validate.page.PageValidate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/dict")
public class SystemDictController {

    private SystemDictService systemDictService;

    public SystemDictController(SystemDictService systemDictService) {
        this.systemDictService = systemDictService;
    }

    /**
     * 获取字典列表
     * */
    @GetMapping("list")
    public AjaxResult<Object> getDictList(@Validated PageValidate pageValidate, @Validated SystemDictSearchValidate searchValidate) {
        return systemDictService.dictList(pageValidate, searchValidate);
    }

    /**
     * 获取所有字典数据
     * */
    @GetMapping("all")
    public AjaxResult<Object> getDictDataList() {
        return systemDictService.allDictList();
    }

    /**
     * 新增字典
     * */
    @PostMapping("add")
    public AjaxResult<Object> addDict(@Validated @RequestBody SystemDictCreateValidate createValidate) {
        return systemDictService.addDict(createValidate);
    }

    /**
     * 获取字典类型详情数据
     * */
    @GetMapping("detail")
    public AjaxResult<Object> getDictDetail(@Validated @RequestParam String id) {
        return systemDictService.getDictDetail(id);
    }

    /**
     * 编辑字典
     * */
    @PostMapping("edit")
    public  AjaxResult<Object> editDict(@Validated @RequestBody SystemDictUpdateValidate updateValidate) {
        return systemDictService.editDict(updateValidate);
    }

    /**
     * 删除字典
     * */
    @PostMapping("delete")
    public AjaxResult<Object> deleteDict(@Validated @RequestParam String id) {
        return systemDictService.deleteDict(id);
    }

    /**
     * 查询字典数据
     * */
    @GetMapping("dataList")
    public AjaxResult<Object> getDictData(@Validated PageValidate pageValidate, @Validated SystemDictDataSearchValidate searchValidate) {
        return systemDictService.dictDataList(pageValidate, searchValidate);
    }

    /**
     * 查询字典数据
     * */
    @GetMapping("allDictData")
    @NotPower
    public AjaxResult<Object> all(@Validated String dictType) {
        return systemDictService.all(dictType);
    }

    /**
     * 新增字典数据
     * */
    @PostMapping("addData")
    public AjaxResult<Object> addDictData(@Validated @RequestBody SystemDictDataCreateValidate createValidate) {
        return systemDictService.addDictData(createValidate);
    }

    /**
     * 编辑字典数据
     * */
    @PostMapping("editData")
    public AjaxResult<Object> editDictData(@Validated @RequestBody SystemDictDataUpdateValidate updateValidate) {
        return systemDictService.editDictData(updateValidate);
    }

    /**
     * 删除字典数据
     * */
    @PostMapping("deleteData")
    public AjaxResult<Object> deleteDictData(@Validated @RequestParam String id) {
        return systemDictService.deleteDictData(id);
    }

    /**
     * 获取字典数据详情
     * */
    @GetMapping("dataDetail")
    public AjaxResult<Object> stateDictData(@Validated String id) {
        return systemDictService.getDictDataDetail(id);
    }
}
