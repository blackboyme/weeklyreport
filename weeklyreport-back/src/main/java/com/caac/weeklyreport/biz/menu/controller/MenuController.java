package com.caac.weeklyreport.biz.menu.controller;

import com.caac.weeklyreport.biz.menu.entity.Menu;
import com.caac.weeklyreport.biz.menu.service.IMenuService;
import com.caac.weeklyreport.biz.user.entity.UserInfo;
import com.caac.weeklyreport.common.ResultBean;
import com.caac.weeklyreport.common.ResultCode;
import com.caac.weeklyreport.util.UserContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@Api(value = "菜单管理",tags="菜单管理")
public class MenuController {

    private final IMenuService IMenuService;

    public MenuController(IMenuService IMenuService) {
        this.IMenuService = IMenuService;
    }

    @ApiOperation("获取当前用户的菜单")
    @GetMapping
    public ResultBean<?> getMenu() {
        UserInfo currentUser = UserContext.getCurrentUser();
        if (currentUser == null) {
            return ResultBean.fail(ResultCode.PARAM_IS_NULL);
        }
        
        List<Menu> menus = IMenuService.getMenusByRoleId(currentUser.getRoleId());
        return ResultBean.success(menus);
    }
}
