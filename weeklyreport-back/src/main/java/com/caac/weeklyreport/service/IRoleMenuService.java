package com.caac.weeklyreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.entity.RoleMenu;

import java.util.List;

/**
 * <p>
 * 角色菜单关联表 服务类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
public interface IRoleMenuService extends IService<RoleMenu> {
    RoleMenu createRoleMenu(RoleMenu roleMenu);
    RoleMenu getRoleMenuById(String id);
    List<RoleMenu> getAllRoleMenus();
    RoleMenu updateRoleMenu(RoleMenu roleMenu);
    void deleteRoleMenu(String id);
    List<RoleMenu> getRoleMenusByRoleId(String roleId);
    List<RoleMenu> getRoleMenusByMenuId(String menuId);
    void batchAssignMenusToRole(String roleId, List<String> menuIds);
}
