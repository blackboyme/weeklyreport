package com.caac.weeklyreport.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.Serializable;
import java.time.LocalDateTime;

public class UserInfo implements Serializable {
    private String userId; // 用户ID
    private String userName; // 员工名称
    private String openId; // openId
    private String teamId; // 团队部门ID
    private String phoneNo; // 手机号
    private String roleId = "1"; // 角色ID
    private String token; // token
    private LocalDateTime createdAt; // 创建时间
    private LocalDateTime updatedAt; // 更新时间
    private String isDeleted = "0"; // 是否删除
    private String roleName; // 角色名称
    private String roleType; // 角色类型
    private String teamName; // 团队名称
    private String departName;
    private String deptId;// 部门名称

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public UserInfo() {
    }

    public UserInfo(String userId, String userName, String openId, String teamId, String phoneNo, String roleId,
                   String roleName, String roleType, String teamName, String departName, String deptId) {
        this.userId = userId;
        this.userName = userName;
        this.openId = openId;
        this.teamId = teamId;
        this.phoneNo = phoneNo;
        this.roleId = roleId;
        this.roleName = roleName;
        this.roleType = roleType;
        this.teamName = teamName;
        this.departName = departName;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.deptId = deptId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }


    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

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