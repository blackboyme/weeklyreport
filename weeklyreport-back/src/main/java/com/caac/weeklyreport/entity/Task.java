package com.caac.weeklyreport.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("task")
public class Task {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    @TableField("user_id")
    private String userId;
    
    private String title;
    private String description;
    private String important;
    private String archived;
    private String completed;
    
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}