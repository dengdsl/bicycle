package com.bicycle.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bicycle.entry.user.SystemUserEntry;
import com.bicycle.mapper.account.SystemConfigMapper;
import com.bicycle.mapper.user.SystemUserMapper;
import com.bicycle.service.user.SystemUserService;
import com.bicycle.utils.AjaxResult;
import com.bicycle.validate.page.PageValidate;
import com.bicycle.validate.user.SystemUserCreateValidate;
import com.bicycle.validate.user.SystemUserSearchValidate;
import com.bicycle.validate.user.SystemUserUpdateValidate;
import com.bicycle.vo.user.SystemUserVo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SystemUserServiceImpl implements SystemUserService {

    private SystemUserMapper userMapper;

    public SystemUserServiceImpl(SystemUserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 获取用户列表
     * */
    @Override
    public AjaxResult<Object> userList(PageValidate pageValidate, SystemUserSearchValidate userSearchValidate) {
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();
        if (pageNo ==  null) {
            pageNo = 1;
        }
        if (pageSize ==  null) {
            pageSize = 15;
        }
        // 创建查询对象
        List<SystemUserEntry> systemUserEntries = userMapper.userList(pageNo - 1, pageSize, userSearchValidate);
        Long integer = userMapper.userCount(userSearchValidate);
        Map<String, Object> result = new HashMap<>();
        result.put("count", integer);
        result.put("lists", systemUserEntries);
        return AjaxResult.success(result);
    }

    /**
     * 新增用户
     * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult<Object> addUser(SystemUserCreateValidate createValidate) {
        // 查询用户名称和账号是否存在
        QueryWrapper<SystemUserEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", createValidate.getNickname()).or().eq("account", createValidate.getAccount());
        SystemUserEntry systemUserEntry = userMapper.selectOne(queryWrapper);
        if (systemUserEntry != null) {
            return AjaxResult.failed("用户名或账号已被占用，请切换重试");
        }
        if (!createValidate.getPasswordConfirm().equals(createValidate.getPassword())){
            return AjaxResult.failed("两次输入的密码不正确");
        }
        // 新增用户
        systemUserEntry = new SystemUserEntry();
        systemUserEntry.setAccount(createValidate.getAccount());
        systemUserEntry.setUsername(createValidate.getNickname());
        systemUserEntry.setPassword(createValidate.getPassword());
        systemUserEntry.setEmail(createValidate.getEmail());
        systemUserEntry.setRemark(createValidate.getRemark());
        systemUserEntry.setAvatar(createValidate.getAvatar());
        systemUserEntry.setUserState(createValidate.getUserState());
        systemUserEntry.setCreateTime(new Date());
        systemUserEntry.setUpdateTime(new Date());
        userMapper.insert(systemUserEntry);

        // 插入用户所属角色
        userMapper.insertRoleList(systemUserEntry.getId(), createValidate.getRoleIds());
        // 插入用户所属部门
        userMapper.insertDeptList(systemUserEntry.getId(), createValidate.getDeptIds());
        return AjaxResult.success("新增成功");
    }

    /**
     * 编辑用户
     * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult<Object> editUser(SystemUserUpdateValidate updateValidate) {
        // 查询用户是否存在
        QueryWrapper<SystemUserEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", updateValidate.getId());
        SystemUserEntry systemUserEntry = userMapper.selectOne(queryWrapper);
        if (systemUserEntry == null) {
            return AjaxResult.failed("用户不存在");
        }

        // 查询用户名称和账号是否存在
        QueryWrapper<SystemUserEntry> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("username", updateValidate.getNickname()).or().eq("account", updateValidate.getAccount());
        SystemUserEntry systemUserEntry1 = userMapper.selectOne(queryWrapper1);
        if (systemUserEntry1 != null && (!systemUserEntry1.getAccount().equals(systemUserEntry.getAccount()) || !systemUserEntry1.getUsername().equals(systemUserEntry.getUsername()))) {
            return AjaxResult.failed("用户名或账号已被占用，请切换重试");
        }

        if (!updateValidate.getPasswordConfirm().equals(updateValidate.getPassword())){
            return AjaxResult.failed("两次输入的密码不正确");
        }
        // 更新用户
        systemUserEntry.setUsername(updateValidate.getNickname());
        systemUserEntry.setAccount(updateValidate.getAccount());
        systemUserEntry.setPassword(updateValidate.getPassword());
        systemUserEntry.setEmail(updateValidate.getEmail());
        systemUserEntry.setRemark(updateValidate.getRemark());
        systemUserEntry.setAvatar(updateValidate.getAvatar());
        systemUserEntry.setUserState(updateValidate.getUserState());
        systemUserEntry.setUpdateTime(new Date());
        userMapper.updateById(systemUserEntry);
        // 删除原有部门信息
        userMapper.deleteDeptList(systemUserEntry.getId());
        // 删除原有的角色信息
        userMapper.deleteRoleList(systemUserEntry.getId());
        // 插入用户所属角色
        userMapper.insertRoleList(systemUserEntry.getId(), updateValidate.getRoleIds());
        // 插入用户所属部门
        userMapper.insertDeptList(systemUserEntry.getId(), updateValidate.getDeptIds());
        return AjaxResult.success("更新成功");
    }

    /**
     * 删除用户
     * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult<Object> deleteUser(Long userId) {
        // 查询用户是否存在
        QueryWrapper<SystemUserEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        SystemUserEntry systemUserEntry = userMapper.selectOne(queryWrapper);
        if (systemUserEntry == null) {
            return AjaxResult.failed("用户不存在");
        }
        // 删除原有部门信息
        userMapper.deleteDeptList(systemUserEntry.getId());
        // 删除原有的角色信息
        userMapper.deleteRoleList(systemUserEntry.getId());
        // 删除用户
        userMapper.deleteById(userId);
        return AjaxResult.success("删除成功");
    }

    /**
     * 获取用户详情
     * */
    @Override
    public AjaxResult<Object> userDetail(Long userId) {
        // 查询用户是否存在
        QueryWrapper<SystemUserEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        SystemUserEntry systemUserEntry = userMapper.selectOne(queryWrapper);
        if (systemUserEntry == null) {
            return AjaxResult.failed("用户不存在");
        }
        List<Long> roleIdList = userMapper.getRoleIdList(userId);
        List<Long> deptIdList = userMapper.getDeptIdList(userId);
        SystemUserVo userVo = new SystemUserVo();
        userVo = userVo.toEntry(systemUserEntry);
        userVo.setRoleIds(roleIdList);
        userVo.setDeptIds(deptIdList);
        return AjaxResult.success(userVo);
    }

    /**
     * 设置用户状态
     * */
    @Override
    public AjaxResult<Object> userStatus(Long userId) {
        // 查询用户是否存在
        QueryWrapper<SystemUserEntry> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        SystemUserEntry systemUserEntry = userMapper.selectOne(queryWrapper);
        if (systemUserEntry == null) {
            return AjaxResult.failed("用户不存在");
        }
        String result;
        if(systemUserEntry.getUserState() == 1) {
            systemUserEntry.setUserState(0);
            result = "启用成功";
        }else {
            systemUserEntry.setUserState(1);
            result = "禁用成功";
        }
        userMapper.updateById(systemUserEntry);
        return AjaxResult.success(result);
    }


}
