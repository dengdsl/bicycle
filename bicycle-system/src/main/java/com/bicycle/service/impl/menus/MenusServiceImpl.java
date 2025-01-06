package com.bicycle.service.impl.menus;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bicycle.entry.menus.SystemMenusEntry;
import com.bicycle.enums.HttpEnum;
import com.bicycle.exception.CustomException;
import com.bicycle.mapper.menus.SystemMenuMapper;
import com.bicycle.service.menus.IMenuService;
import com.bicycle.utils.AjaxResult;
import com.bicycle.utils.ArrayUtils;
import com.bicycle.validate.menus.CreateMenuValidate;
import com.bicycle.validate.menus.UpdateMenuValidate;
import com.bicycle.vo.menus.SystemMenusVo;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MenusServiceImpl implements IMenuService {


    @Resource
    private SystemMenuMapper menuMapper;

    /**
     * 获取菜单列表
     * */
    @Override
    public AjaxResult<Object> menus(Long adminId) {
        List<SystemMenusEntry> systemMenusEntries;
        // 如果是超级管理员，查询所有菜单
        if (adminId == 1) {
            QueryWrapper<SystemMenusEntry> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByDesc("menu_sort");
            systemMenusEntries = menuMapper.selectList(queryWrapper);
        }else {
            // 查询用户菜单列表
            systemMenusEntries = menuMapper.queryUserMenus(adminId);
            // 是否存在菜单列表
        }
        List<SystemMenusVo> menusVoList = new ArrayList<>();
        for (SystemMenusEntry systemMenusEntry : systemMenusEntries) {
            SystemMenusVo menusVo = new SystemMenusVo();
            menusVo.setId(systemMenusEntry.getId());
            menusVo.setPid(systemMenusEntry.getPid());
            menusVo.setMenuType(systemMenusEntry.getMenuType());
            menusVo.setMenuName(systemMenusEntry.getMenuName());
            menusVo.setMenuIcon(systemMenusEntry.getMenuIcon());
            menusVo.setMenuSort(systemMenusEntry.getMenuSort());
            menusVo.setPerms(systemMenusEntry.getPerms());
            menusVo.setPaths(systemMenusEntry.getPaths());
            menusVo.setComponent(systemMenusEntry.getComponent());
            menusVo.setIsCache(systemMenusEntry.getIsCache());
            menusVo.setIsShow(systemMenusEntry.getIsShow());
            menusVo.setIsDisable(systemMenusEntry.getIsDisable());
            menusVo.setSelected(systemMenusEntry.getSelected());
            menusVo.setUpdateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(systemMenusEntry.getUpdateTime()));
            menusVoList.add(menusVo);
        }
        String jsonString = JSONArray.toJSONString(menusVoList, SerializerFeature.WriteMapNullValue);
        JSONArray jsonArray = JSONArray.parseArray(jsonString);
        // 调用工具类将菜单列表转换为树型结构
        JSONArray jsonArrayList = ArrayUtils.listToTree(jsonArray, "id", "pid", "children");
        return AjaxResult.success(jsonArrayList);
    }

    /**
     * 新增菜单按钮
     * */
    @Override
    public void addMenu(CreateMenuValidate validate) {
        SystemMenusEntry menusEntry = new SystemMenusEntry();
        BeanUtils.copyProperties(validate, menusEntry);
        menusEntry.setCreateTime(new Date());
        menusEntry.setUpdateTime(new Date());
        menuMapper.insert(menusEntry);
    }

    /**
     * 编辑菜单按钮
     * */
    @Override
    public void editMenu(UpdateMenuValidate validate) {
        // 查询菜单是否存在
        SystemMenusEntry menusEntry = menuMapper.selectById(validate.getId());
        if (menusEntry == null) {
            throw new CustomException(HttpEnum.FAILED.getCode(), "菜单不存在");
        }
        // 将传递过来的数据保存进去
        menusEntry.setPid(validate.getPid());
        menusEntry.setMenuType(validate.getMenuType());
        menusEntry.setMenuName(validate.getMenuName());
        menusEntry.setMenuIcon(validate.getMenuIcon());
        menusEntry.setMenuSort(validate.getMenuSort());
        menusEntry.setPerms(validate.getPerms());
        menusEntry.setPaths(validate.getPaths());
        menusEntry.setComponent(validate.getComponent());
        menusEntry.setSelected(validate.getSelected());
        menusEntry.setParams(validate.getParams());
        menusEntry.setIsCache(validate.getIsCache());
        menusEntry.setIsShow(validate.getIsShow());
        menusEntry.setIsDisable(validate.getIsDisable());
        menusEntry.setUpdateTime(new Date());
        menuMapper.updateById(menusEntry);
    }

    /**
     * 获取菜单详情
     * */
    @Override
    public SystemMenusVo getMenuDetail(Long id) {
        SystemMenusEntry systemMenusEntry = menuMapper.selectById(id);
        SystemMenusVo menusVo = new SystemMenusVo();
        BeanUtils.copyProperties(systemMenusEntry, menusVo);
        return menusVo;
    }

    /**
     * 删除菜单
     * */
    @Override
    public void delMenu(Long id) {
        menuMapper.deleteById(id);
    }
}
