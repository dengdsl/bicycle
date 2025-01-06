package com.bicycle.controller.department;

import com.bicycle.service.department.DepartmentService;
import com.bicycle.utils.AjaxResult;
import com.bicycle.validate.department.DepartmentCreateValidate;
import com.bicycle.validate.department.DepartmentSearchValidate;
import com.bicycle.validate.department.DepartmentUpdateValidate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/system")
public class DepartmentController {

    private DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * 获取部门信息列表
     * */
    @GetMapping("deptList")
    public AjaxResult<Object> departmentList(@Validated DepartmentSearchValidate searchValidate) {
        return departmentService.departmentList(searchValidate);
    }

    /**
     * 新增部门信息
     * */
    @PostMapping("deptAdd")
    public AjaxResult<Object> departmentAdd(@Validated @RequestBody DepartmentCreateValidate createValidate) {
        return departmentService.departmentAdd(createValidate);
    }

    /**
     * 修改部门信息
     * */
    @PostMapping("deptEdit")
    public AjaxResult<Object> departmentEdit(@Validated @RequestBody DepartmentUpdateValidate updateValidate) {
        return departmentService.departmentEdit(updateValidate);
    }

    /**
     * 删除部门信息
     * */
    @PostMapping("deptDelete")
    public AjaxResult<Object> departmentDelete(@Validated Long id) {
        return departmentService.departmentDelete(id);
    }

    /**
     * 获取部门信息详情
     * */
    @GetMapping("deptDetail")
    public AjaxResult<Object> departmentDetail(@Validated Long id) {
        return departmentService.departmentDetail(id);
    }

}
