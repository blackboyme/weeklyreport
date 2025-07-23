package com.caac.weeklyreport.biz.menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.biz.menu.entity.Menu;

import java.util.List;

public interface IMenuService extends IService<Menu> {
    List<Menu> getMenusByRoleId(String roleId);
    List<Menu> buildMenuTree(List<Menu> menus);
} 