package com.caac.weeklyreport.biz.personalReport.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 个人周报表
 * </p>
 *
 * @author hanrenjie
 * @since 2025-07-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("personal_report")
@ApiModel(value="PersonalReport对象", description="个人周报表")
public class PersonalReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "周报ID")
    @TableId(value = "pr_id", type = IdType.NONE)
    private String prId;

    @ApiModelProperty(value = "员工ID")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "员工名称")
    @TableField("user_name")
    private String userName;

    @ApiModelProperty(value = "团队ID")
    @TableField("team_id")
    private String teamId;

    @ApiModelProperty(value = "团队名称")
    @TableField("team_name")
    private String teamName;

    @ApiModelProperty(value = "部门ID")
    @TableField("dept_id")
    private String deptId;

    @ApiModelProperty(value = "部门名称")
    @TableField("dept_name")
    private String deptName;

    @ApiModelProperty(value = "开始日期")
    @TableField("start_date")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "结束日期")
    @TableField("end_date")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "周数")
    @TableField("week")
    private Integer week;

    @ApiModelProperty(value = "本周主要工作")
    @TableField("primary")
    private String primary;

    @ApiModelProperty(value = "本周主要工作特殊说明")
    @TableField("special_primary")
    private String specialPrimary;

    @ApiModelProperty(value = "本周示范区建设")
    @TableField("construction")
    private String construction;

    @ApiModelProperty(value = "本周示范区建设特殊说明")
    @TableField("special_construction")
    private String specialConstruction;

    @ApiModelProperty(value = "本周其他工作")
    @TableField("others")
    private String others;

    @ApiModelProperty(value = "本周其他工作特殊说明")
    @TableField("special_others")
    private String specialOthers;

    @ApiModelProperty(value = "下周主要工作")
    @TableField("next_primary")
    private String nextPrimary;

    @ApiModelProperty(value = "下周主要工作特殊说明")
    @TableField("next_special_primary")
    private String nextSpecialPrimary;

    @ApiModelProperty(value = "下周示范区建设")
    @TableField("next_construction")
    private String nextConstruction;

    @ApiModelProperty(value = "下周示范区建设特殊说明")
    @TableField("next_special_construction")
    private String nextSpecialConstruction;

    @ApiModelProperty(value = "下周其他工作")
    @TableField("next_others")
    private String nextOthers;

    @ApiModelProperty(value = "下周其他工作特殊说明")
    @TableField("next_special_others")
    private String nextSpecialOthers;

    @ApiModelProperty(value = "当前状态：1-保存,2-已提交")
    @TableField("current_status")
    private String currentStatus;

    @ApiModelProperty(value = "创建时间")
    @TableField("created_at")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @ApiModelProperty(value = "是否删除(0-正常,1-删除)")
    @TableField("is_deleted")
    private String isDeleted;


}
