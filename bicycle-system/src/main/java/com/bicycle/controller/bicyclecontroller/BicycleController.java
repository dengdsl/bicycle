package com.bicycle.controller.bicyclecontroller;


import com.bicycle.annotation.NotLogin;
import com.bicycle.annotation.NotPower;
import com.bicycle.service.bicycle.BicycleService;
import com.bicycle.utils.AjaxResult;
import com.bicycle.validate.bicycle.ExportValidate;
import com.bicycle.validate.page.PageValidate;
import com.bicycle.validate.bicycle.BicycleCreateValidate;
import com.bicycle.validate.bicycle.BicycleUpdateValidate;
import com.bicycle.validate.bicycle.BicycleSearchValidate;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
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
     * 获取自行车列表数据
     * */
    @GetMapping("list")
    public AjaxResult<Object> getBicycleList(@Validated PageValidate pageValidate, @Validated BicycleSearchValidate searchValidate){
        return bicycleService.getBicycleList(pageValidate, searchValidate);
    }

    /**
     * 新增自行车信息
     * */
    @PostMapping("add")
    public AjaxResult<Object> addBicycle(@NotNull HttpServletRequest request, @Validated @RequestBody BicycleCreateValidate create){
        return bicycleService.addBicycle(create, request);
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
     * 下载导入模版
     * */
    @GetMapping("download/template")
    public void downloadTemplate(@NotNull HttpServletResponse response) throws IOException, DecoderException {
         bicycleService.downloadImportTemplate(response);
    }
    /**
     * 批量导入
     * */
    @PostMapping("import")
    public AjaxResult<Object> importBicycle(@NotNull HttpServletRequest request, @Validated @RequestParam MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return AjaxResult.failed("上传文件不能为空");
        }
        String originalFilename = file.getOriginalFilename();
        // 判断上传的文件是不是xlsx后缀
        if (!originalFilename.endsWith(".xlsx")) {
            return AjaxResult.failed("请上传xlsx格式的文件");
        }

        return bicycleService.importBicycle(file, request);
    }

    /**
     * 批量导出
     * */
    @PostMapping("export")
    public void exportBicycle(@NotNull HttpServletResponse response, @Validated @RequestBody ExportValidate exportValidate) throws IOException {
         bicycleService.exportBicycle(response, exportValidate.getIds());
    }

    /**
     * 移动端查询接口
     * */
    @NotPower
    @NotLogin
    @GetMapping("query")
    public AjaxResult<Object> query(@Validated @RequestParam String qrcode ){
        return bicycleService.queryByQrcode(qrcode);
    }

    /**
     * 批量下载二维码，通过压缩包的方式返回
     * */
    @PostMapping("download/qrcode")
    public void downloadQrcode(@NotNull HttpServletResponse response, @Validated @RequestBody ExportValidate exportValidate) throws IOException {
        bicycleService.downloadQrcode(response, exportValidate.getIds());
    }

    /**
     * 批量下载二维码编号
     * */
    @PostMapping("download/frameNo")
    public void downloadFrameNo(@NotNull HttpServletResponse response, @Validated @RequestBody ExportValidate exportValidate) throws IOException {
        bicycleService.downloadFrameNo(response, exportValidate.getIds());
    }

    /**
     * 重新生成二维码
     * */
    @PostMapping("reset/qrcode")
    public AjaxResult<Object> resetQrcode(@Validated String id)  {
        return bicycleService.resetQrcode(id);
    }

}

