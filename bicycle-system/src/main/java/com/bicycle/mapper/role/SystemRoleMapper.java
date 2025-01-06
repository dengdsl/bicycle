package com.bicycle.mapper.role;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bicycle.entry.role.SystemRoleEntry;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SystemRoleMapper extends BaseMapper<SystemRoleEntry> {

    /**
     * 删除角色拥有的权限
     * */
    void deleteRoleAuth(@Param("id") Long id);
    /**
     * 给角色分配权限
     * */
    void setRoleAuth(@Param("id") Long id, @Param("menuIds") String[] menuIds);

    /**
     * 获取角色权限列表
     * */
    List<Integer> queryRoleMenus(@Param("id") Long id);
}
