package com.bicycle.service.roleservice;

import com.bicycle.utils.AjaxResult;
import com.bicycle.validate.page.PageValidate;
import com.bicycle.validate.role.SystemRoleAuthValidate;
import com.bicycle.validate.role.SystemRoleCreateValidate;
import com.bicycle.validate.role.SystemRoleUpdateValidate;

public interface RoleService {
    /**
     * 获取角色列表，需要进行分页
     * */
    AjaxResult<Object> getRoleLists(PageValidate pageValidate);

    /**
     * 获取所有角色
     * */
    AjaxResult<Object> getRoles();

    /**
     * 新增角色
     * */
    AjaxResult<Object> roleAdd(SystemRoleCreateValidate createValidate);

    /**
     * 编辑
     * */
    AjaxResult<Object> roleEdit(SystemRoleUpdateValidate updateValidate);

    /**
     * 删除角色
     * */
    AjaxResult<Object> roleDelete(Long id);

    /**
     * 获取角色详情
     * */
    AjaxResult<Object> roleDetail(Long id);

    /**
     * 角色授权
     * */
    AjaxResult<Object> roleAuth(SystemRoleAuthValidate validate);

    /**
     * 获取角色权限列表
     * */
    AjaxResult<Object> getRoleAuth(Long id);
}
