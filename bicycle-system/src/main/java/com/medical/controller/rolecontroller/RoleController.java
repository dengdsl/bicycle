package com.medical.controller.rolecontroller;


import com.medical.service.roleservice.RoleService;
import com.medical.utils.AjaxResult;
import com.medical.validate.page.PageValidate;
import com.medical.validate.role.SystemRoleAuthValidate;
import com.medical.validate.role.SystemRoleCreateValidate;
import com.medical.validate.role.SystemRoleUpdateValidate;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/system")
public class RoleController {

    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 获取角色列表，需要进行分页
     * */
    @GetMapping("roleLists")
    public AjaxResult<Object> getRoleLists(@Validated PageValidate pageValidate){
        return roleService.getRoleLists(pageValidate);
    }

    /**
     * 获取所有的角色
     * */
    @GetMapping("roleAll")
    public AjaxResult<Object> getRoles(){
        return roleService.getRoles();
    }

    /**
     * 新增角色
     * */
    @PostMapping("addRole")
    public AjaxResult<Object> addRole(@Validated @RequestBody SystemRoleCreateValidate createValidate){
        return roleService.roleAdd(createValidate);
    }

    /**
     * 编辑角色
     * */
    @PostMapping("editRole")
    public AjaxResult<Object> editRole(@Validated @RequestBody SystemRoleUpdateValidate updateValidate){
        return roleService.roleEdit(updateValidate);
    }

    /**
     * 获取角色详情
     * */
    @GetMapping("roleDetail")
    public AjaxResult<Object> getRoleDetail(@Validated @NotNull(message = "角色id必传") Long id){
        return roleService.roleDetail(id);
    }

    /**
     * 删除角色
     * */
    @PostMapping("roleDelete")
    public AjaxResult<Object> deleteRole(@Validated @RequestParam Long id){
        return roleService.roleDelete(id);
    }

    /**
     * 角色授权
     * */
    @PostMapping("roleAuth")
    public AjaxResult<Object> grantRole(@Validated @RequestBody SystemRoleAuthValidate validate){
        return roleService.roleAuth(validate);
    }

    /**
     * 获取角色权限列表
     * */
    @GetMapping("roleMenus")
    public AjaxResult<Object> getRoleMenus(@Validated @NotNull(message = "角色id必传") Long id){
        return roleService.getRoleAuth(id);
    }

}

