package com.caac.weeklyreport.biz.teamReport.entity;

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
 * 团队周报表
 * </p>
 *
 * @author hanrenjie
 * @since 2025-07-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("team_report")
@ApiModel(value="TeamReport对象", description="团队周报表")
public class TeamReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "周报ID")
    @TableId(value = "tr_id", type = IdType.NONE)
    private String trId;

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

    @ApiModelProperty(value = "周数")
    @TableField("year")
    private Integer year;

    @ApiModelProperty(value = "本周主要工作")
    @TableField("summary")
    private String summary;

    @ApiModelProperty(value = "本周主要工作")
    @TableField("next_summary")
    private String nextSummary;

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
