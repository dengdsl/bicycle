package com.bicycle.controller.uploadcontroller;

import com.bicycle.annotation.NotLogin;
import com.bicycle.annotation.NotPower;
import com.bicycle.utils.AjaxResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Random;

@RestController
@Slf4j
@RequestMapping("api/upload")
public class ImageUploadController {

    /**
     * 上传图片
     *
     * @param file 请求对象
     */
    @NotPower
    @NotLogin
    @PostMapping("/image")
    public AjaxResult<Object> image(HttpServletRequest request, @Validated @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return AjaxResult.failed("文件不能为空");
        }
        // 生成时间戳
        long timestamp = System.currentTimeMillis() / 1000;
        // 生成随机数
        int randomNum = new Random().nextInt(90000) + 10000;
        // 获取文件名
        String fileName =  timestamp + randomNum + file.getOriginalFilename();
        log.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        log.info("上传的后缀名为：" + suffixName);
        // 文件上传后的路径
        String filePath = "F:\\workspace\\bicycle\\bicycle-system\\src\\main\\resources\\static\\userAvatar\\";
        //String filePath = "/home/server/static/userAvatar/";
        // 解决中文问题，liunx下中文路径，图片显示问题

        File dest = new File(filePath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        // 拼接服务器地址
        String serverName = request.getServerName();
        String url = "http://" + serverName + "/static/userAvatar/" + fileName;
        log.info("头像保存的路径信息：" + url);
        try {
            file.transferTo(dest);
            return AjaxResult.success("文件上传成功", url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.failed("文件上传失败");
    }

    /**
     * 上传图片
     *
     * @param file 请求对象
     */
    @NotPower
    @NotLogin
    @PostMapping("/file")
    public AjaxResult<Object> file(HttpServletRequest request, @Validated @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return AjaxResult.failed("文件不能为空");
        }
        // 生成时间戳
        long timestamp = System.currentTimeMillis() / 1000;
        // 生成随机数
        int randomNum = new Random().nextInt(90000) + 10000;
        // 获取文件名
        String fileName =  timestamp + randomNum + file.getOriginalFilename();
        log.info("上传的文件名为：" + fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        log.info("上传的后缀名为：" + suffixName);
        // 文件上传后的路径
//        String filePath = "D:\\onecent_code\\bicycle\\bicycle-system\\src\\main\\resources\\static\\file\\";
        String filePath = "/home/server/static/file/";
        File dest = new File(filePath + fileName);
        // 检测是否存在目录
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        // 拼接服务器地址
        String serverName = request.getServerName();
        int port = request.getServerPort();
        String contextPath = request.getContextPath(); // 自动获取上下文路径
        String url = "http://" + serverName + "/static/file/" + fileName;
        log.info("头像保存的路径信息：" + url);
        try {
            file.transferTo(dest);
            return AjaxResult.success("文件上传成功", url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.failed("文件上传失败");
    }
}
