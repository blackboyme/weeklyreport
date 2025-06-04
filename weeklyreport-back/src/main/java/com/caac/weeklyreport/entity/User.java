package com.caac.weeklyreport.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    @TableId
    private String userId;          // 用户ID
    private String userName;        // 员工名称
    private String teamId;          // 团队部门ID
    private String phoneNo;         // 手机号
    private String roleId = "1";    // 角色ID
    private String token;           // token
    private LocalDateTime createdAt;    // 创建时间
    private LocalDateTime updatedAt;    // 更新时间
    private String isDeleted = "0"; // 是否删除
}
