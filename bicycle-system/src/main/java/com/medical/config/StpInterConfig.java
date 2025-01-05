package com.medical.config;

import cn.dev33.satoken.stp.StpInterface;
import com.medical.entry.menus.SystemMenusEntry;
import com.medical.mapper.menus.SystemMenuMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StpInterConfig implements StpInterface {

    @Resource
    private SystemMenuMapper menuMapper;

    /**
     * 用于获取一个账号拥有的权限码集合
     * @param loginId 用户登录id
     * @param loginType 用户登录类型
     * @return 权限集合
     * */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 获取角色具有的权限集合
        Long loginIdLong = Long.parseLong((String) loginId);
        List<String> collect = menuMapper.queryUserPermission(loginIdLong).stream()

                .map(SystemMenusEntry::getPerms).collect(Collectors.toList());
        return collect;
    }


    /**
     * 返回一个账号拥有的角色标识集合(权限与角色分开校验)
     * @param loginId 用户登录id
     * @param loginType 用户登录类型
     * @return 角色标识集合
     * */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return null;
    }

    /**
     * 在初始化后执行，将系统中的所有角色对应的权限信息设置到sa-token中进行保存
     */
    @PostConstruct
    public void init() {
        //
        System.out.println("+=================================项目初始化的时候已经执行了=================================");
    }
}
