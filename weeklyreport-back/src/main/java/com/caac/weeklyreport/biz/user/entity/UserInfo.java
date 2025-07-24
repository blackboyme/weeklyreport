package com.caac.weeklyreport.biz.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserInfo {
    private String userId; // 用户ID
    private String userName; // 员工名称
    private String openId; // openId
    private String teamId; // 团队部门ID
    private String phoneNo; // 手机号
    private String roleId = "1"; // 角色ID
    private String token; // token
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createdAt; // 创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime updatedAt; // 更新时间
    private String isDeleted = "0"; // 是否删除
    private String roleName; // 角色名称
    private String roleType; // 角色类型
    private String teamName; // 团队名称
    private String deptName;
    private String deptId;// 部门名称
    private String teamTitle;// 填写标题

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public String toJsonString() {
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException("Error converting UserInfo to JSON", e);
        }
    }

    public static UserInfo fromJsonString(String json) {
        try {
            return objectMapper.readValue(json, UserInfo.class);
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to UserInfo", e);
        }
    }
} 