package com.caac.weeklyreport.biz.menu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.biz.menu.entity.Menu;
import com.caac.weeklyreport.biz.menu.mapper.MenuMapper;
import com.caac.weeklyreport.biz.menu.service.IMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    private final MenuMapper menuMapper;

    public MenuServiceImpl(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    @Override
    public List<Menu> getMenusByRoleId(String roleId) {
        List<Menu> menus = menuMapper.getMenusByRoleId(roleId);
        return buildMenuTree(menus);
    }

    @Override
    public List<Menu> buildMenuTree(List<Menu> menus) {
        List<Menu> trees = new ArrayList<>();
        Map<String, Menu> menuMap = new HashMap<>();
        
        // 将所有菜单放入map中
        for (Menu menu : menus) {
            menuMap.put(menu.getMenuId(), menu);
        }
        
        // 构建树形结构
        for (Menu menu : menus) {
            String parentId = menu.getParentId();
            if (parentId == null || "0".equals(parentId)) {
                // 这是一个根节点
                trees.add(menu);
            } else {
                // 这是一个子节点
                Menu parent = menuMap.get(parentId);
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(menu);
                }
            }
        }
        
        return trees;
    }
}
