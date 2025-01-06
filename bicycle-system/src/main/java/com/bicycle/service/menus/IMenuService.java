package com.bicycle.service.menus;

import com.bicycle.utils.AjaxResult;
import com.bicycle.validate.menus.CreateMenuValidate;
import com.bicycle.validate.menus.UpdateMenuValidate;
import com.bicycle.vo.menus.SystemMenusVo;

public interface IMenuService {

    /**
     * 获取路由菜单列表
     * */
    AjaxResult<Object> menus(Long adminId);

    /**
     * 新增路由菜单
     * */
    void addMenu(CreateMenuValidate validate);
    /**
     * 编辑路由菜单
     * */
    void editMenu(UpdateMenuValidate validate);

    /**
     * 获取路由菜单详情
     * */
    SystemMenusVo getMenuDetail(Long id);

    /**
     * 删除菜单按钮
     * */
    void delMenu(Long id);
}
