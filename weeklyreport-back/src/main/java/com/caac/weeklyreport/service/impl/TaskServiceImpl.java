package com.caac.weeklyreport.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.caac.weeklyreport.entity.Task;
import com.caac.weeklyreport.mapper.TaskMapper;
import com.caac.weeklyreport.service.TaskService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Override
    public Task getUpdatedTask(Task task) {
        boolean updated = this.updateById(task);
        if (updated) {
            return this.getById(task.getId());
        }
        return null;
    }

    @Override
    public List<Task> getAllTasksByUserId(String userId) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return this.baseMapper.selectList(queryWrapper);
    }
}
