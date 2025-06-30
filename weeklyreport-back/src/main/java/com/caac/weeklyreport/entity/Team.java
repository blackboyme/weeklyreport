package com.caac.weeklyreport.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

/**
 * <p>
 * 团队部门表
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-22
 */
@TableName("team")
public class Team implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 团队部门ID
     */
    @TableId(value = "team_id", type = IdType.NONE)
    private String teamId;

    /**
     * 团队名称
     */
    @TableField("team_name")
    private String teamName;

    /**
     * 部门名称
     */
    @TableField("depart_name")
    private String departName;

    /**
     * 创建时间
     */
    @TableField("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime updatedAt;

    /**
     * 是否删除(0-正常,1-删除)
     */
    @TableField("is_deleted")
    private String isDeleted;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
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

    @Override
    public String toString() {
        return "Team{" +
            "teamId=" + teamId +
            ", teamName=" + teamName +
            ", departName=" + departName +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", isDeleted=" + isDeleted +
        "}";
    }
}
