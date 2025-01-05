package com.medical.controller.bicyclecontroller;


import com.medical.service.bicycle.BicycleService;
import com.medical.utils.AjaxResult;
import com.medical.validate.page.PageValidate;
import com.medical.validate.bicycle.BicycleCreateValidate;
import com.medical.validate.bicycle.BicycleUpdateValidate;
import com.medical.validate.bicycle.BicycleSearchValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 供应商表实体类
 * */
@Slf4j
@RestController
@RequestMapping("api/bicycle")
public class BicycleController  {

    private BicycleService bicycleService;

    public BicycleController(BicycleService supplierService) {
        this.bicycleService = supplierService;
    }

    /**
     * 获取供应商列表
     * */
    @GetMapping("list")
    public AjaxResult<Object> getBicycleList(@Validated PageValidate pageValidate, @Validated BicycleSearchValidate searchValidate){
        return bicycleService.getBicycleList(pageValidate, searchValidate);
    }

    /**
     * 新增自行车信息
     * */
    @PostMapping("add")
    public AjaxResult<Object> getAllSupplier(@Validated @RequestBody BicycleCreateValidate create){
        return bicycleService.addBicycle(create);
    }

    /**
     * 获取供应商详情
     * */
    @PostMapping("edit")
    public AjaxResult<Object> editBicycle(@Validated @RequestBody BicycleUpdateValidate update){
        return bicycleService.editBicycle(update);
    }

    /**
     * 删除自行车信息
     * */
    @PostMapping("delete")
    public AjaxResult<Object> deleteBicycle(@Validated String id){
        return bicycleService.deleteBicycle(id);
    }

    /**
     * 获取自行车详情数据
     * */
    @GetMapping("detail")
    public AjaxResult<Object> getBicycle(@Validated String id){
        return bicycleService.getBicycleDetail(id);
    }

    /**
     * 批量导入
     * */
    @PostMapping("import")
    public AjaxResult<Object> importBicycle(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return AjaxResult.failed("上传文件不能为空");
        }
        return bicycleService.importBicycle(file);
    }

    /**
     * 切换供应商状态
     * */
    @PostMapping("export")
    public AjaxResult<Object> exportBicycle(@Validated @RequestParam String id){
        return bicycleService.exportBicycle();
    }
}

