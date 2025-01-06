package com.bicycle.service.impl.department;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bicycle.entry.department.DepartmentEntry;
import com.bicycle.entry.menus.SystemMenusEntry;
import com.bicycle.mapper.department.DepartmentMapper;
import com.bicycle.service.department.DepartmentService;
import com.bicycle.utils.AjaxResult;
import com.bicycle.utils.ArrayUtils;
import com.bicycle.validate.department.DepartmentCreateValidate;
import com.bicycle.validate.department.DepartmentSearchValidate;
import com.bicycle.validate.department.DepartmentUpdateValidate;
import com.bicycle.vo.departtment.DepartmentVo;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentMapper departmentMapper;

    public DepartmentServiceImpl(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    /**
     * 获取部门信息列表
     * */
    @Override
    public AjaxResult<Object> departmentList(DepartmentSearchValidate searchValidate) {
        //创建查询对象
        QueryWrapper<DepartmentEntry> queryWrapper = new QueryWrapper<>();
        // 设置搜索参数
        if (searchValidate.getDeptName() != null && !searchValidate.getDeptName().isEmpty()) {
            queryWrapper.like("dept_name", searchValidate.getDeptName());
        }
        if ( searchValidate.getDeptState() != null) {
            queryWrapper.eq("is_stop", searchValidate.getDeptState());
        }

        queryWrapper.orderByDesc("sort");
        // 执行查询
        List<DepartmentEntry> departmentEntries = departmentMapper.selectList(queryWrapper);
        List<DepartmentVo> list = new ArrayList<>();
        for (DepartmentEntry departmentEntry : departmentEntries) {
            DepartmentVo department = new DepartmentVo();
            department.setId(departmentEntry.getId());
            department.setParentId(departmentEntry.getParentId());
            department.setDeptName(departmentEntry.getDeptName());
            department.setDuty(departmentEntry.getDuty());
            department.setMobile(departmentEntry.getMobile());
            department.setSort(departmentEntry.getSort());
            department.setIsStop(departmentEntry.getIsStop());
            department.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(departmentEntry.getUpdateTime()));
            list.add(department);
        }
        // 将查询到的数据转换为树型结构
        String jsonString = JSONArray.toJSONString(list, SerializerFeature.WriteMapNullValue);
        JSONArray jsonArray = JSONArray.parseArray(jsonString);
        // 调用工具类将菜单列表转换为树型结构
        JSONArray jsonArrayList = ArrayUtils.listToTree(jsonArray, "id", "parentId", "children");
        return AjaxResult.success(jsonArrayList);
    }

    /**
     * 新增部门信息
     * */
    @Override
    public AjaxResult<Object> departmentAdd(DepartmentCreateValidate createValidate) {
        QueryWrapper<DepartmentEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("dept_name", createValidate.getDeptName());
        queryWrapper.eq("parent_id", createValidate.getParentId());
        // 查询当前部门是否存在
        DepartmentEntry departmentEntry = departmentMapper.selectOne(queryWrapper);
        if (departmentEntry != null) {
            return AjaxResult.failed("部门已存在");
        }
        // 创建部门信息
        departmentEntry = new DepartmentEntry();
        departmentEntry.setDeptName(createValidate.getDeptName());
        departmentEntry.setParentId(createValidate.getParentId());
        departmentEntry.setDuty(createValidate.getDuty());
        departmentEntry.setMobile(createValidate.getMobile());
        departmentEntry.setIsStop(createValidate.getIsStop());
        departmentEntry.setSort(createValidate.getSort());
        departmentEntry.setCreateTime(new Date());
        departmentEntry.setUpdateTime(new Date());
        departmentMapper.insert(departmentEntry);
        return AjaxResult.success("新增成功");
    }

    /**
     * 修改部门信息
     * */
    @Override
    public AjaxResult<Object> departmentEdit(DepartmentUpdateValidate updateValidate) {
        // 查询部门信息是否存在
        QueryWrapper<DepartmentEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", updateValidate.getId());
        // 查询当前部门是否存在
        DepartmentEntry departmentEntry = departmentMapper.selectOne(queryWrapper);
        if (departmentEntry == null) {
            return AjaxResult.failed( "部门不存在");
        }
        // 修改部门信息
        departmentEntry.setParentId(updateValidate.getParentId());
        departmentEntry.setDeptName(updateValidate.getDeptName());
        departmentEntry.setDuty(updateValidate.getDuty());
        departmentEntry.setMobile(updateValidate.getMobile());
        departmentEntry.setIsStop(updateValidate.getIsStop());
        departmentEntry.setSort(updateValidate.getSort());
        departmentEntry.setUpdateTime(new Date());
        departmentMapper.updateById(departmentEntry);
        return AjaxResult.success("编辑成功");
    }

    /**
     * 删除部门信息
     * */
    @Override
    public AjaxResult<Object> departmentDelete(Long id) {
        // 查询部门信息是否存在
        QueryWrapper<DepartmentEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        // 查询当前部门是否存在
        DepartmentEntry departmentEntry = departmentMapper.selectOne(queryWrapper);
        if (departmentEntry == null) {
            return AjaxResult.failed( "部门不存在");
        }
        // 查询当前部门是否存在下级
        QueryWrapper<DepartmentEntry> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("parent_id", id);
        List<DepartmentEntry> departmentEntries = departmentMapper.selectList(queryWrapper1);
        if (departmentEntries!= null && departmentEntries.size() > 0) {
            return AjaxResult.failed("请先删除下级部门");
        }
        // 调用方法删除部门信息
        departmentMapper.deleteById(id);
        // 删除用户部门表中对应的信息
        return AjaxResult.success("删除成功");
    }

    /**
     * 获取部门信息详情
     * */
    @Override
    public AjaxResult<Object> departmentDetail(Long id) {
        // 查询部门信息是否存在
        QueryWrapper<DepartmentEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        // 查询当前部门是否存在
        DepartmentEntry departmentEntry = departmentMapper.selectOne(queryWrapper);
        return AjaxResult.success(departmentEntry);
    }
}
