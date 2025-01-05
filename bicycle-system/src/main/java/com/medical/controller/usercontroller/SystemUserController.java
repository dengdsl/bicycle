package com.medical.controller.usercontroller;

import cn.dev33.satoken.stp.StpUtil;
import com.medical.service.user.SystemUserService;
import com.medical.utils.AjaxResult;
import com.medical.validate.page.PageValidate;
import com.medical.validate.user.SystemUserCreateValidate;
import com.medical.validate.user.SystemUserSearchValidate;
import com.medical.validate.user.SystemUserUpdateValidate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/system")
public class SystemUserController {

    private SystemUserService userService;

    public SystemUserController(SystemUserService userService) {
        this.userService = userService;
    }

    /**
     * 获取用户列表
     * */
    @GetMapping("/userLists")
    public AjaxResult<Object> userList(@Validated PageValidate pageValidate, SystemUserSearchValidate searchValidate){
        return userService.userList(pageValidate, searchValidate);
    }

    /**
     * 新增用户信息
     * */
    @PostMapping("/addUser")
    public AjaxResult<Object> addUser(@Validated @RequestBody SystemUserCreateValidate createValidate){
        return userService.addUser(createValidate);
    }

    /**
     * 编辑用户信息
     * */
    @PostMapping("/editUser")
    public AjaxResult<Object> editUser(@Validated @RequestBody SystemUserUpdateValidate updateValidate){
        return userService.editUser(updateValidate);
    }

    /**
     * 删除用户信息
     * */
    @PostMapping("/deleteUser")
    public AjaxResult<Object> deleteUser(){
        Object loginId = StpUtil.getLoginId();
        long id = Long.parseLong(loginId.toString());
        return userService.deleteUser(id);
    }

    /**
     * 获取用户详情
     * */
    @GetMapping("userDetail")
    public AjaxResult<Object> userDetail(Long id){
        return userService.userDetail(id);
    }

    /**
     * 启用/禁用用户管理员
     * */
    @GetMapping("userStatus")
    public AjaxResult<Object> userStatus(Long id){
        return userService.userStatus(id);
    }
}
