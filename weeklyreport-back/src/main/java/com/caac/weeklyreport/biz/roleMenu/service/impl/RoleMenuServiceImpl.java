package com.caac.weeklyreport.biz.roleMenu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.biz.roleMenu.entity.RoleMenu;
import com.caac.weeklyreport.biz.roleMenu.mapper.RoleMenuMapper;
import com.caac.weeklyreport.biz.roleMenu.service.IRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 角色菜单关联表 服务实现类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

    private final RoleMenuMapper roleMenuMapper;

    public RoleMenuServiceImpl(RoleMenuMapper roleMenuMapper) {
        this.roleMenuMapper = roleMenuMapper;
    }

    @Override
    public RoleMenu createRoleMenu(RoleMenu roleMenu) {
        roleMenu.setRoleMenuId(UUID.randomUUID().toString());
        roleMenu.setCreatedAt(LocalDateTime.now());
        roleMenu.setUpdatedAt(LocalDateTime.now());
        roleMenu.setIsDeleted("0");
        roleMenuMapper.insert(roleMenu);
        return getRoleMenuById(roleMenu.getRoleMenuId());
    }

    @Override
    public RoleMenu getRoleMenuById(String id) {
        return roleMenuMapper.selectById(id);
    }

    @Override
    public List<RoleMenu> getAllRoleMenus() {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", "0");
        return roleMenuMapper.selectList(queryWrapper);
    }

    @Override
    public RoleMenu updateRoleMenu(RoleMenu roleMenu) {
        roleMenu.setUpdatedAt(LocalDateTime.now());
        roleMenuMapper.updateById(roleMenu);
        return getRoleMenuById(roleMenu.getRoleMenuId());
    }

    @Override
    public void deleteRoleMenu(String id) {
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRoleMenuId(id);
        roleMenu.setIsDeleted("1");
        roleMenu.setUpdatedAt(LocalDateTime.now());
        roleMenuMapper.updateById(roleMenu);
    }

    @Override
    public List<RoleMenu> getRoleMenusByRoleId(String roleId) {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId)
                   .eq("is_deleted", "0");
        return roleMenuMapper.selectList(queryWrapper);
    }

    @Override
    public List<RoleMenu> getRoleMenusByMenuId(String menuId) {
        QueryWrapper<RoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("menu_id", menuId)
                   .eq("is_deleted", "0");
        return roleMenuMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void batchAssignMenusToRole(String roleId, List<String> menuIds) {
        // 先删除该角色的所有菜单关联
        QueryWrapper<RoleMenu> deleteWrapper = new QueryWrapper<>();
        deleteWrapper.eq("role_id", roleId);
        roleMenuMapper.delete(deleteWrapper);

        // 批量插入新的关联关系
        for (String menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleMenuId(UUID.randomUUID().toString());
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenu.setCreatedAt(LocalDateTime.now());
            roleMenu.setUpdatedAt(LocalDateTime.now());
            roleMenu.setIsDeleted("0");
            roleMenuMapper.insert(roleMenu);
        }
    }
}
