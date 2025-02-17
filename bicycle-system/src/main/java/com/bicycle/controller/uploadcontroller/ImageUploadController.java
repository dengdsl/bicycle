package com.bicycle.controller.uploadcontroller;

import com.bicycle.annotation.NotLogin;
import com.bicycle.annotation.NotPower;
import com.bicycle.entry.system.SystemConfigEntry;
import com.bicycle.mapper.account.SystemConfigMapper;
import com.bicycle.service.bicycle.BicycleService;
import com.bicycle.service.impl.bicycle.BicycleServiceImpl;
import com.bicycle.utils.AjaxResult;
import com.bicycle.utils.ConfigUtils;
import com.bicycle.utils.FilePathUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;

@RestController
@Slf4j
@RequestMapping("api/upload")
public class ImageUploadController{

    /**
     * 上传图片
     *
     * @param file 请求对象
     */
    @NotPower
    @NotLogin
    @PostMapping("/image")
    public AjaxResult<Object> image(HttpServletRequest request, @Validated @RequestParam("file") MultipartFile file, @RequestParam("path") String path) {
        if (file.isEmpty()) {
            return AjaxResult.failed("文件不能为空");
        }
        if (path == null) {
            path = "";
        }
        // 判断上传的文件是否是图片
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        log.info("上传的后缀名为：" + suffixName);
        if (!Arrays.asList(".jpg", ".png", ".jpeg", ".bmp", ".gif", ".webp", ".tiff").contains(suffixName.toLowerCase())) {
            return AjaxResult.failed("请上传图片格式的文件");
        }

        String basePath = FilePathUtil.generateBasePath(path);

        // 生成时间戳
        long timestamp = System.currentTimeMillis() / 1000;
        // 生成随机数
        int randomNum = new Random().nextInt(90000) + 10000;
        // 获取文件名
        fileName = timestamp + randomNum + fileName;
        log.info("上传的文件名为：" + fileName);

        // 解决中文问题，liunx下中文路径，图片显示问题
        File dest = new File(String.format("%s/%s/%s", ConfigUtils.getFilePath(), basePath, fileName));
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        //String serverUrl = ConfigUtils.getServerUrl();
        //if (serverUrl == null) {
        //    serverUrl = request.getScheme() + "://" + request.getServerName() +"/static";
        //}
        // 拼接服务器地址
        String url = String.format("/static/%s/%s", basePath, fileName);
        log.info("图片上传路径：" + url);
        try {
            file.transferTo(dest);
            return AjaxResult.success("文件上传成功", url);
        } catch (Exception e) {
            log.error("文件上传失败: " + e.getMessage());
            return AjaxResult.failed("文件上传失败：" + e.getMessage());
        }
    }

    /**
     * 上传图片
     *
     * @param file 请求对象
     */
    @NotPower
    @NotLogin
    @PostMapping("/file")
    public AjaxResult<Object> file(HttpServletRequest request, @Validated @RequestParam("file") MultipartFile file, String path) {
        if (file.isEmpty()) {
            return AjaxResult.failed("文件不能为空");
        }
        if (path == null || path.isEmpty()) {
            path = "file";
        }
        String basePath = FilePathUtil.generateBasePath(path);

        // 生成时间戳
        long timestamp = System.currentTimeMillis() / 1000;

        // 生成随机数
        int randomNum = new Random().nextInt(90000) + 10000;
        // 获取文件名
        String fileName = timestamp + randomNum + file.getOriginalFilename();
        log.info("上传的文件名为：" + fileName);

        // 解决中文问题，liunx下中文路径，图片显示问题
        File dest = new File(String.format("%s/%s/%s", ConfigUtils.getFilePath(), basePath, fileName));
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        //String serverUrl = ConfigUtils.getServerUrl();
        //if (serverUrl == null) {
        //    serverUrl = request.getScheme() + "://" + request.getServerName() + "/static";
        //}
        // 拼接服务器地址
        String url = String.format("/static/%s/%s", basePath, fileName);
        try {
            file.transferTo(dest);
            return AjaxResult.success("文件上传成功", url);
        } catch (Exception e) {
            log.error("文件上传失败: " + e.getMessage());
            return AjaxResult.failed("文件上传失败: " + e.getMessage());
        }
    }
}
