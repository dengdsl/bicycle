package com.medical.service.department;

import com.medical.utils.AjaxResult;
import com.medical.validate.department.DepartmentCreateValidate;
import com.medical.validate.department.DepartmentSearchValidate;
import com.medical.validate.department.DepartmentUpdateValidate;

public interface DepartmentService {

    /**
     * 获取部门信息列表
     * */
     AjaxResult<Object> departmentList(DepartmentSearchValidate searchValidate);

    /**
     * 新增部门信息
     * */
     AjaxResult<Object> departmentAdd(DepartmentCreateValidate createValidate);

    /**
     * 修改部门信息
     * */
     AjaxResult<Object> departmentEdit(DepartmentUpdateValidate updateValidate);

    /**
     * 删除部门信息
     * */
     AjaxResult<Object> departmentDelete(Long id);

    /**
     * 获取部门信息详情
     * */
     AjaxResult<Object> departmentDetail(Long id);
}
