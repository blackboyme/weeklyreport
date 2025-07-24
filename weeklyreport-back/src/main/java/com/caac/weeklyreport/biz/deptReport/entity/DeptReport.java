package com.caac.weeklyreport.biz.deptReport.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * 部门周报表
 * </p>
 *
 * @author hanrenjie
 * @since 2025-07-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dept_report")
@ApiModel(value="DeptReport对象", description="部门周报表")
public class DeptReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "周报ID")
    @TableId(value = "dr_id", type = IdType.NONE)
    private String drId;

    @ApiModelProperty(value = "员工ID")
    private String userId;

    @ApiModelProperty(value = "员工名称")
    private String userName;

    @ApiModelProperty(value = "团队ID")
    private String teamId;

    @ApiModelProperty(value = "团队名称")
    private String teamName;

    @ApiModelProperty(value = "部门ID")
    private String deptId;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "开始日期")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "结束日期")
    private LocalDateTime endDate;

    @ApiModelProperty(value = "周数")
    private Integer week;

    @ApiModelProperty(value = "本周主要工作")
    private String summary;

    @ApiModelProperty(value = "本周主要工作")
    private String nextSummary;

    @ApiModelProperty(value = "当前状态：1-保存,2-已提交")
    private String currentStatus;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updatedAt;

    @ApiModelProperty(value = "是否删除(0-正常,1-删除)")
    private String isDeleted;


}
