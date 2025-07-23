package com.caac.weeklyreport.biz.role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.biz.role.entity.Role;
import com.caac.weeklyreport.biz.role.mapper.RoleMapper;
import com.caac.weeklyreport.biz.role.service.IRoleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public Role createRole(Role role) {
        role.setRoleId(UUID.randomUUID().toString());
        role.setCreatedAt(LocalDateTime.now());
        role.setUpdatedAt(LocalDateTime.now());
        role.setIsDeleted("0");
        roleMapper.insert(role);
        return getRoleById(role.getRoleId());
    }

    @Override
    public Role getRoleById(String id) {
        return roleMapper.selectById(id);
    }

    @Override
    public List<Role> getAllRoles() {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", "0");
        return roleMapper.selectList(queryWrapper);
    }

    @Override
    public Role updateRole(Role role) {
        role.setUpdatedAt(LocalDateTime.now());
        roleMapper.updateById(role);
        return getRoleById(role.getRoleId());
    }

    @Override
    public void deleteRole(String id) {
        Role role = new Role();
        role.setRoleId(id);
        role.setIsDeleted("1");
        role.setUpdatedAt(LocalDateTime.now());
        roleMapper.updateById(role);
    }

    @Override
    public List<Role> getRolesByType(String roleType) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_type", roleType)
                   .eq("is_deleted", "0");
        return roleMapper.selectList(queryWrapper);
    }
}
