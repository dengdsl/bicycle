package com.medical.service.impl.role;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.medical.entry.role.SystemRoleEntry;
import com.medical.mapper.role.SystemRoleMapper;
import com.medical.service.roleservice.RoleService;
import com.medical.utils.AjaxResult;
import com.medical.validate.page.PageValidate;
import com.medical.validate.role.SystemRoleAuthValidate;
import com.medical.validate.role.SystemRoleCreateValidate;
import com.medical.validate.role.SystemRoleUpdateValidate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 角色实现类
 * */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    private SystemRoleMapper roleMapper;

    public RoleServiceImpl(SystemRoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    /**
     * 获取角色列表，需要进行分页
     * */
    @Override
    public AjaxResult<Object> getRoleLists(PageValidate pageValidate) {
        // 判断分页参数是否传递
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();
        if (pageNo == null) {
            pageNo = 1;
        }
        if (pageSize == null) {
            pageSize = 15;
        }
        // 创建查询对象
        Page<SystemRoleEntry> page = new Page<>(pageNo, pageSize);
        QueryWrapper<SystemRoleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("create_time");
        // 执行查询
        IPage<SystemRoleEntry> roleIPage = roleMapper.selectPage(page, queryWrapper);
        List<SystemRoleEntry> roleEntries = roleIPage.getRecords();
        long total = roleIPage.getTotal();
        Map<String, Object> result = new HashMap<>();
        result.put("count", total);
        result.put("lists", roleEntries);
        // 返回结果
        return AjaxResult.success(result);
    }

    /**
     * 获取所有的角色
     * */
    @Override
    public AjaxResult<Object> getRoles() {
        QueryWrapper<SystemRoleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        List<SystemRoleEntry> roleEntries = roleMapper.selectList(queryWrapper);
        return AjaxResult.success(roleEntries);
    }

    /**
     * 新增角色
     * */
    @Override
    public AjaxResult<Object> roleAdd(SystemRoleCreateValidate createValidate) {
        // 查询角色信息是否存在
        SystemRoleEntry roleEntry = roleMapper.selectOne(new QueryWrapper<SystemRoleEntry>().eq("role_name", createValidate.getRoleName()));
        if (roleEntry!= null) {
            return AjaxResult.failed("角色已存在");
        }
        roleEntry = new SystemRoleEntry();
        roleEntry.setRoleName(createValidate.getRoleName());
        roleEntry.setSort(createValidate.getSort());
        roleEntry.setRoleCount(0L);
        roleEntry.setRoleState(createValidate.getRoleState());
        roleEntry.setRemark(createValidate.getRemark());
        roleEntry.setCreateTime(new Date());
        roleMapper.insert(roleEntry);
        return AjaxResult.success("新增成功");
    }

    /**
     * 编辑角色
     * */
    @Override
    public AjaxResult<Object> roleEdit(SystemRoleUpdateValidate updateValidate) {
        // 查询角色是否存在
        SystemRoleEntry roleEntry = roleMapper.selectById(updateValidate.getId());
        if (roleEntry == null) {
            return AjaxResult.failed("角色不存在");
        }
        roleEntry.setRoleName(updateValidate.getRoleName());
        roleEntry.setSort(updateValidate.getSort());
        roleEntry.setRoleState(updateValidate.getRoleState());
        roleEntry.setRemark(updateValidate.getRemark());
        roleMapper.updateById(roleEntry);
        return AjaxResult.success("编辑成功");
    }

    /**
     * 删除角色
     * */
    @Override
    public AjaxResult<Object> roleDelete(Long id) {
        // 查询角色是否存在
        SystemRoleEntry roleEntry = roleMapper.selectById(id);
        if (roleEntry == null) {
            return AjaxResult.failed("角色不存在");
        }
        roleMapper.deleteById(id);
        // 需要注意删除用户角色表中的数据信息
        return AjaxResult.success("删除成功");
    }

    /**
     * 获取角色详情
     * */
    @Override
    public AjaxResult<Object> roleDetail(Long id) {
        // 创建查询对象
        QueryWrapper<SystemRoleEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        // 获取角色信息
        SystemRoleEntry roleEntry = roleMapper.selectOne(queryWrapper);
        return AjaxResult.success(roleEntry);
    }

    /**
     * 角色授权
     * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult<Object> roleAuth(SystemRoleAuthValidate validate) {
        // 将菜单id转换为数组类型
        String[] ids = validate.getMenuIds().split(",");
        // 将当前角色原有的权限删除
        roleMapper.deleteRoleAuth(validate.getId());
        roleMapper.setRoleAuth(validate.getId(), ids);
        return AjaxResult.success("设置成功");
    }

    /**
     * 获取角色权限列表
     * */
    @Override
    public AjaxResult<Object> getRoleAuth(Long id) {
        // 获取角色权限列表
        return AjaxResult.success(roleMapper.queryRoleMenus(id));
    }
}
