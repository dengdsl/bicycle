package com.bicycle.mapper.menus;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bicycle.entry.menus.SystemMenusEntry;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SystemMenuMapper  extends BaseMapper<SystemMenusEntry> {

    /**
     * 根据用户id与角色相关信息查询用户菜单
     * */
    List<SystemMenusEntry> queryUserPermission(@Param("adminId") Long adminId);

    /**
     * 根据用户id查询当前用户下的是所有菜单
     * */
    List<SystemMenusEntry> queryUserMenus(@Param("adminId") Long adminId);

    /**
     * 根据用户id查询所有菜单列表
     * */
    List<SystemMenusEntry> queryUserRoutes(@Param("adminId") Long adminId);
}
