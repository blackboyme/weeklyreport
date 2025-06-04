package com.caac.weeklyreport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caac.weeklyreport.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 不需要在这里定义方法，BaseMapper已经提供了基本的CRUD操作
}
