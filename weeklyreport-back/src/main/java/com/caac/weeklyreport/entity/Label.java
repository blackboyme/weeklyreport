package com.caac.weeklyreport.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("label")
public class Label {
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    
    @TableField("userId")
    private String userId;
    
    private String name;
    
    @TableField("createdAt")
    private LocalDateTime createdAt;
    
    @TableField("updatedAt")
    private LocalDateTime updatedAt;
}