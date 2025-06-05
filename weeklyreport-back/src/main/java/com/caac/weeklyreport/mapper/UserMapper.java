package com.caac.weeklyreport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caac.weeklyreport.entity.User;
import com.caac.weeklyreport.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 不需要在这里定义方法，BaseMapper已经提供了基本的CRUD操作
    
    @Select("SELECT " +
            "u.user_id, u.user_name, u.team_id, u.phone_no, u.role_id, " +
            "u.created_at, u.updated_at, u.is_deleted, " +
            "r.role_name, r.role_type, " +
            "t.team_name, t.depart_name " +
            "FROM user u " +
            "LEFT JOIN role r ON u.role_id = r.role_id " +
            "LEFT JOIN team t ON u.team_id = t.team_id " +
            "WHERE u.phone_no = #{phoneNo} AND u.is_deleted = '0'")
    UserInfo getUserInfoByPhoneNo(String phoneNo);
}
