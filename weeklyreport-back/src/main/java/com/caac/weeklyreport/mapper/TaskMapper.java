package com.caac.weeklyreport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caac.weeklyreport.entity.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<Task> {
    List<Task> findTasksByUserId(String userId);
}