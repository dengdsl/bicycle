package com.bicycle.controller.bicyclecontroller;


import com.bicycle.annotation.NotLogin;
import com.bicycle.annotation.NotPower;
import com.bicycle.service.bicycle.BicycleService;
import com.bicycle.utils.AjaxResult;
import com.bicycle.validate.page.PageValidate;
import com.bicycle.validate.bicycle.BicycleCreateValidate;
import com.bicycle.validate.bicycle.BicycleUpdateValidate;
import com.bicycle.validate.bicycle.BicycleSearchValidate;
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
     * 批量导出
     * */
    @PostMapping("export")
    public AjaxResult<Object> exportBicycle(@Validated @RequestParam String id){
        return bicycleService.exportBicycle();
    }

    /**
     * 移动端查询接口
     * */
    @NotPower
    @NotLogin
    @PostMapping("query")
    public AjaxResult<Object> query(){
        return null;
    }
}

