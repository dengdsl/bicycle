package com.medical.service.user;

import com.medical.utils.AjaxResult;
import com.medical.validate.page.PageValidate;
import com.medical.validate.user.SystemUserCreateValidate;
import com.medical.validate.user.SystemUserSearchValidate;
import com.medical.validate.user.SystemUserUpdateValidate;

public interface SystemUserService {

    /**
     * 获取用户列表
     * */
    AjaxResult<Object> userList(PageValidate pageValidate, SystemUserSearchValidate searchValidate);

    /**
     * 新增用户
     * */
    AjaxResult<Object> addUser(SystemUserCreateValidate createValidate);

    /**
     * 编辑用户
     * */
    AjaxResult<Object> editUser(SystemUserUpdateValidate updateValidate);

    /**
     * 删除用户
     * */
    AjaxResult<Object> deleteUser(Long userId);

    /**
     * 获取用户详情
     * */
    AjaxResult<Object> userDetail(Long UserId);

    /**
     * 设置用户状态
     * */
    AjaxResult<Object> userStatus(Long userId);
}
