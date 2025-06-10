package com.caac.weeklyreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.entity.Menu;

import java.util.List;

public interface MenuService extends IService<Menu> {
    List<Menu> getMenusByRoleId(String roleId);
    List<Menu> buildMenuTree(List<Menu> menus);
} 