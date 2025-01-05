package com.medical.controller.menuscontroller;


import cn.dev33.satoken.stp.StpUtil;
import com.medical.service.menus.IMenuService;
import com.medical.utils.AjaxResult;
import com.medical.validate.menus.CreateMenuValidate;
import com.medical.validate.menus.UpdateMenuValidate;
import com.medical.vo.menus.SystemMenusVo;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("api/system")
public class MenusController {

    @Resource
    private IMenuService menuService;

    /**
     * 获取菜单列表
     * */
    @GetMapping("/menus")
    public AjaxResult<Object> menus(){
        String loginId = (String) StpUtil.getLoginId();
        return  menuService.menus(Long.parseLong(loginId));
    }

    /**
     * 添加菜单
     * */
    @PostMapping("/menu/add")
    public AjaxResult<Object> addMenu(@Validated @RequestBody CreateMenuValidate createValidate){
        menuService.addMenu(createValidate);
        return AjaxResult.success();
    }

    /**
     * 编辑菜单
     * */
    @PostMapping("/menu/edit")
    public AjaxResult<Object> editMenu(@Validated @RequestBody UpdateMenuValidate updateValidate){
        menuService.editMenu(updateValidate);
        return AjaxResult.success();
    }

    /**
     * 获取菜单详情
     * */
    @GetMapping("/menu/detail")
    public AjaxResult<Object> getMenuDetail(@RequestParam("id") Long id){
        SystemMenusVo menusVo = menuService.getMenuDetail(id);
        return AjaxResult.success(menusVo);
    }

    /**
     * 删除菜单
     * */
    @PostMapping("/menu/del")
    public AjaxResult<Object> delMenu(@RequestParam("id") Long id) {
        menuService.delMenu(id);
        return AjaxResult.success();
    }
}
