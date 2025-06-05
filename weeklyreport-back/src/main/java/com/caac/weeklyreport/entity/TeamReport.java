package com.caac.weeklyreport.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 团队周报表
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
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

    @ApiModelProperty(value = "本周装备研发")
    @TableField("equip")
    private String equip;

    @ApiModelProperty(value = "本周系统开发")
    @TableField("system_rd")
    private String systemRd;

    @ApiModelProperty(value = "本周示范区建设")
    @TableField("construction")
    private String construction;

    @ApiModelProperty(value = "本周其他工作")
    @TableField("others")
    private String others;

    @ApiModelProperty(value = "下周装备研发")
    @TableField("next_equip")
    private String nextEquip;

    @ApiModelProperty(value = "下周系统开发")
    @TableField("next_system")
    private String nextSystem;

    @ApiModelProperty(value = "下周示范区建设")
    @TableField("next_construction")
    private String nextConstruction;

    @ApiModelProperty(value = "下周其他工作")
    @TableField("next_others")
    private String nextOthers;

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
