package com.caac.weeklyreport.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
@ApiModel(value="User对象", description="用户表")
public class User {
    @TableId
    private String userId;          // 用户ID
    private String userName;        // 员工名称
    private String openId;        // openID
    private String teamId;          // 团队部门ID
    private String deptId;          //部门ID
    private String phoneNo;         // 手机号
    private String roleId = "1";    // 角色ID
    private String token;           // token
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createdAt;    // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime updatedAt;    // 更新时间
    private String isDeleted = "0"; // 是否删除
}
