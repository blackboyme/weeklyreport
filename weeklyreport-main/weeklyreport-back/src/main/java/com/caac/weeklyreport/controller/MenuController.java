package com.caac.weeklyreport.controller;

import com.caac.weeklyreport.ResultBean;
import com.caac.weeklyreport.common.ResultCode;
import com.caac.weeklyreport.entity.Menu;
import com.caac.weeklyreport.entity.UserInfo;
import com.caac.weeklyreport.service.MenuService;
import com.caac.weeklyreport.util.UserContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public ResultBean<?> getMenu() {
        UserInfo currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            return ResultBean.fail(ResultCode.PARAM_IS_NULL);
        }
        
        List<Menu> menus = menuService.getMenusByRoleId(currentUser.getRoleId());
        return ResultBean.success(menus);
    }
}
