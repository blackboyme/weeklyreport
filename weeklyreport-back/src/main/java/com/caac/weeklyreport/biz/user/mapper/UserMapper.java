package com.caac.weeklyreport.biz.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caac.weeklyreport.biz.user.entity.User;
import com.caac.weeklyreport.biz.user.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 不需要在这里定义方法，BaseMapper已经提供了基本的CRUD操作
    
    @Select("SELECT " +
            "u.user_id, u.user_name,u.open_id, u.team_id, u.phone_no, u.role_id, u.token, " +
            "u.created_at, u.updated_at, u.is_deleted, " +
            "r.role_name, r.role_type, " +
            "t.team_name,d.dept_id,d.dept_name " +
            "FROM user u " +
            "LEFT JOIN role r ON u.role_id = r.role_id " +
            "LEFT JOIN team t ON u.team_id = t.team_id " +
            "LEFT JOIN dept d ON u.dept_id = d.dept_id " +
            "WHERE u.phone_no = #{phoneNo} AND u.is_deleted = '0'")
    UserInfo getUserInfoByPhoneNo(@Param("phoneNo")String phoneNo);

    @Select("SELECT u.* " +
            "FROM user u " +
            "WHERE u.team_id =  #{teamId} " +
            "AND u.is_deleted = '0' " +
            "AND u.role_id = ( " +
            "SELECT r.role_id " +
            "FROM role r " +
            "WHERE r.role_type = #{roleType} LIMIT 1) ")
    User getTeamApprover(@Param("teamId") String teamId, @Param("roleType") String roleType);

    @Select("SELECT u.* " +
            "FROM user u " +
            "WHERE u.is_deleted = '0' " +
            "AND u.role_id = ( " +
            "SELECT r.role_id " +
            "FROM role r " +
            "WHERE r.role_type = #{roleType} LIMIT 1) ")
    User getDeptApprover(@Param("roleType")String roleType);

}
