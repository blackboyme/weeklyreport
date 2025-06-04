package com.caac.weeklyreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.entity.Task;

import java.util.List;

public interface TaskService extends IService<Task> {
    // 移除 updateById 方法的声明，因为它已经在 IService 中定义
    // 添加一个新方法来获取更新后的任务
    Task getUpdatedTask(Task task);

    List<Task> getAllTasksByUserId(String userId);
}
