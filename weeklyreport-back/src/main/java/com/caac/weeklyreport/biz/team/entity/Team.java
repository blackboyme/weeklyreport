package com.caac.weeklyreport.biz.team.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 团队部门表
 * </p>
 *
 * @author hanrenjie
 * @since 2025-07-26
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
     * 部门ID
     */
    @TableField("dept_id")
    private String deptId;

    /**
     * 部门名称
     */
    @TableField("dept_name")
    private String deptName;

    /**
     * 填写标题
     */
    @TableField("team_title")
    private String teamTitle;

    /**
     * 创建时间
     */
    @TableField("created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
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
    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public String getTeamTitle() {
        return teamTitle;
    }

    public void setTeamTitle(String teamTitle) {
        this.teamTitle = teamTitle;
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
            ", deptId=" + deptId +
            ", deptName=" + deptName +
            ", teamTitle=" + teamTitle +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", isDeleted=" + isDeleted +
        "}";
    }
}
