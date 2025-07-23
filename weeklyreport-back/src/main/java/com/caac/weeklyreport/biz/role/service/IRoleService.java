package com.caac.weeklyreport.biz.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.biz.role.entity.Role;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
public interface IRoleService extends IService<Role> {
    Role createRole(Role role);
    Role getRoleById(String id);
    List<Role> getAllRoles();
    Role updateRole(Role role);
    void deleteRole(String id);
    List<Role> getRolesByType(String roleType);
}
