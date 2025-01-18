package com.bicycle.controller.uploadcontroller;

import com.bicycle.annotation.NotLogin;
import com.bicycle.annotation.NotPower;
import com.bicycle.entry.system.SystemConfigEntry;
import com.bicycle.mapper.account.SystemConfigMapper;
import com.bicycle.utils.AjaxResult;
import com.bicycle.utils.ConfigUtils;
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

        // 生成时间戳
        long timestamp = System.currentTimeMillis() / 1000;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 月份从0开始，需加1
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 补零处理
        String formattedMonth = (month < 10 ? "0" : "") + month;
        String formattedDay = (day < 10 ? "0" : "") + day;
        // 生成随机数
        int randomNum = new Random().nextInt(90000) + 10000;
        // 获取文件名
        fileName = timestamp + randomNum + fileName;
        log.info("上传的文件名为：" + fileName);

        // 解决中文问题，liunx下中文路径，图片显示问题
        File dest = new File(String.format("%s/%s/%d/%s/%s/%s", ConfigUtils.getFilePath(), path, year,formattedMonth, formattedDay, fileName));
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        String serverUrl = ConfigUtils.getServerUrl();
        if (serverUrl == null) {
            serverUrl = "http://" + request.getServerName() + "/static/";
        }
        // 拼接服务器地址
        String url = String.format("%s/%s/%d/%s/%s/%s", serverUrl, path, year,formattedMonth, formattedDay, fileName);
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
        // 生成时间戳
        long timestamp = System.currentTimeMillis() / 1000;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 月份从0开始，需加1
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // 补零处理
        String formattedMonth = (month < 10 ? "0" : "") + month;
        String formattedDay = (day < 10 ? "0" : "") + day;
        // 生成随机数
        int randomNum = new Random().nextInt(90000) + 10000;
        // 获取文件名
        String fileName = timestamp + randomNum + file.getOriginalFilename();
        log.info("上传的文件名为：" + fileName);

        // 解决中文问题，liunx下中文路径，图片显示问题
        File dest = new File(String.format("%s/%s/%d/%s/%s/%s", ConfigUtils.getFilePath(), path, year,formattedMonth, formattedDay, fileName));
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        String serverUrl = ConfigUtils.getServerUrl();
        if (serverUrl == null) {
            serverUrl = "http://" + request.getServerName() + "/static";
        }
        // 拼接服务器地址
        String url = String.format("%s/%s/%d/%s/%s/%s", serverUrl, path, year,formattedMonth, formattedDay, fileName);
        try {
            file.transferTo(dest);
            return AjaxResult.success("文件上传成功", url);
        } catch (Exception e) {
            log.error("文件上传失败: " + e.getMessage());
            return AjaxResult.failed("文件上传失败: " + e.getMessage());
        }

    }


}
