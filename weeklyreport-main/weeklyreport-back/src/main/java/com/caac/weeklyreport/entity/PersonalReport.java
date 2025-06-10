package com.caac.weeklyreport.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 个人周报表
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-09
 */
@TableName("personal_report")
public class PersonalReport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 周报ID
     */
    @TableId(value = "pr_id", type = IdType.NONE)
    private String prId;

    /**
     * 流程ID
     */
    @TableField("flow_id")
    private String flowId;

    /**
     * 员工ID
     */
    @TableField("user_id")
    private String userId;

    /**
     * 员工名称
     */
    @TableField("user_name")
    private String userName;

    /**
     * 团队ID
     */
    @TableField("team_id")
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
     * 开始日期
     */
    @TableField("start_date")
    private LocalDateTime startDate;

    /**
     * 结束日期
     */
    @TableField("end_date")
    private LocalDateTime endDate;

    /**
     * 周数
     */
    @TableField("week")
    private Integer week;

    /**
     * 本周装备研发
     */
    @TableField("equip")
    private String equip;

    /**
     * 本周系统开发
     */
    @TableField("system_rd")
    private String systemRd;

    /**
     * 本周示范区建设
     */
    @TableField("construction")
    private String construction;

    /**
     * 本周其他工作
     */
    @TableField("others")
    private String others;

    /**
     * 下周装备研发
     */
    @TableField("next_equip")
    private String nextEquip;

    /**
     * 下周系统开发
     */
    @TableField("next_system")
    private String nextSystem;

    /**
     * 下周示范区建设
     */
    @TableField("next_construction")
    private String nextConstruction;

    /**
     * 下周其他工作
     */
    @TableField("next_others")
    private String nextOthers;

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

    public String getPrId() {
        return prId;
    }

    public void setPrId(String prId) {
        this.prId = prId;
    }
    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }
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
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    public Integer getWeek() {
        return week;
    }

    public void setWeek(Integer week) {
        this.week = week;
    }
    public String getEquip() {
        return equip;
    }

    public void setEquip(String equip) {
        this.equip = equip;
    }
    public String getSystemRd() {
        return systemRd;
    }

    public void setSystemRd(String systemRd) {
        this.systemRd = systemRd;
    }
    public String getConstruction() {
        return construction;
    }

    public void setConstruction(String construction) {
        this.construction = construction;
    }
    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }
    public String getNextEquip() {
        return nextEquip;
    }

    public void setNextEquip(String nextEquip) {
        this.nextEquip = nextEquip;
    }
    public String getNextSystem() {
        return nextSystem;
    }

    public void setNextSystem(String nextSystem) {
        this.nextSystem = nextSystem;
    }
    public String getNextConstruction() {
        return nextConstruction;
    }

    public void setNextConstruction(String nextConstruction) {
        this.nextConstruction = nextConstruction;
    }
    public String getNextOthers() {
        return nextOthers;
    }

    public void setNextOthers(String nextOthers) {
        this.nextOthers = nextOthers;
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
        return "PersonalReport{" +
            "prId=" + prId +
            ", flowId=" + flowId +
            ", userId=" + userId +
            ", userName=" + userName +
            ", teamId=" + teamId +
            ", teamName=" + teamName +
            ", deptId=" + deptId +
            ", deptName=" + deptName +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", week=" + week +
            ", equip=" + equip +
            ", systemRd=" + systemRd +
            ", construction=" + construction +
            ", others=" + others +
            ", nextEquip=" + nextEquip +
            ", nextSystem=" + nextSystem +
            ", nextConstruction=" + nextConstruction +
            ", nextOthers=" + nextOthers +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            ", isDeleted=" + isDeleted +
        "}";
    }
}
