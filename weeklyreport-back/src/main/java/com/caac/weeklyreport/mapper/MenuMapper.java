package com.caac.weeklyreport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caac.weeklyreport.entity.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    
    @Select("SELECT m.* FROM menu m " +
            "INNER JOIN role_menu rm ON m.menu_id = rm.menu_id " +
            "WHERE rm.role_id = #{roleId} " +
            "AND m.is_deleted = '0' " +
            "ORDER BY m.sort_order")
    List<Menu> getMenusByRoleId(String roleId);
}
