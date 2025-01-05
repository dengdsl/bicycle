package com.medical.controller.accountcontroller;

import cn.dev33.satoken.stp.StpUtil;
import com.medical.annotation.NotLogin;
import com.medical.annotation.NotPower;
import com.medical.service.account.IAccountService;
import com.medical.utils.AjaxResult;
import com.medical.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("api/system")
public class AccountController {

    private IAccountService accountService;

    public AccountController(IAccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 获取图片验证码
     * */
    @NotLogin  // 免登录验证
    @NotPower
    @GetMapping("/captcha")
    public AjaxResult<Object> getCaptcha(){
        return accountService.captcha();
    }

    /**
     * 登录
     * */
    @NotLogin  // 免登录验证
    @NotPower
    @PostMapping ("/login")
    public AjaxResult<Object> login(@RequestParam("loginValidate") String loginValidate) {
        // 将加密的参数进行解密
        try {
            String validate = RSAUtils.decryptWithPrivate(loginValidate);
            return accountService.login(validate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取密码加密公钥
     * */
    @NotLogin  // 免登录验证
    @NotPower
    @GetMapping("/publicKey")
    public AjaxResult<Object> getPublicKey(){
        String publicKey = RSAUtils.getPublicKey();
        Map<String, String> map = new HashMap<>();
        map.put("publicKey", publicKey);
        return AjaxResult.success(map);
    }

    /**
     * 获取应用配置信息
     * */
    @NotLogin
    @NotPower  // 免登录验证
    @GetMapping("/config")
    public AjaxResult<Object> config(){
        return accountService.config();
    }

    /**
     * 退出登录
     * */
    @NotPower
    @PostMapping("/logout")
    public AjaxResult<Object> logout(){
        return AjaxResult.success();
    }

    /**
     * 获取用户信息以及权限信息
     * */
    @NotPower
    @GetMapping("/self")
    public AjaxResult<Object> self(){
        String loginId = (String) StpUtil.getLoginId();
        return accountService.self(Long.parseLong(loginId));
    }

    /**
     * 获取路由菜单
     * */
    @NotPower
    @GetMapping("routes")
    public AjaxResult<Object> routes(){
        String loginId = (String) StpUtil.getLoginId();
        return accountService.routes(Long.parseLong(loginId));
    }

}
