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
 * 流程历史记录表
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("flow_history")
@ApiModel(value="FlowHistory对象", description="流程历史记录表")
public class FlowHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "历史记录ID")
    @TableId(value = "history_id", type = IdType.NONE)
    private String historyId;

    @ApiModelProperty(value = "流程ID")
    @TableField("flow_id")
    private String flowId;

    @ApiModelProperty(value = "周报ID")
    @TableField("report_id")
    private String reportId;

    @ApiModelProperty(value = "周报类型：1-个人周报,2-团队周报,3-部门周报")
    @TableField("report_type")
    private String reportType;

    @ApiModelProperty(value = "操作类型：1-保存为草稿,2-提交,3-通过,4-退回")
    @TableField("operation")
    private String operation;

    @ApiModelProperty(value = "操作人ID")
    @TableField("operator_id")
    private String operatorId;

    @ApiModelProperty(value = "操作人姓名")
    @TableField("operator_name")
    private String operatorName;

    @ApiModelProperty(value = "操作人角色")
    @TableField("operator_role")
    private String operatorRole;

    @ApiModelProperty(value = "当前环节：1-员工提交,2-团队审核,3-部门审核")
    @TableField("current_stage")
    private String currentStage;

    @ApiModelProperty(value = "上一环节：1-员工提交,2-团队审核,3-部门审核")
    @TableField("last_stage")
    private String lastStage;

    @ApiModelProperty(value = "审批意见")
    @TableField("comment")
    private String comment;

    @ApiModelProperty(value = "创建时间")
    @TableField("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "更新时间")
    @TableField("updated_at")
    private LocalDateTime updatedAt;

    @ApiModelProperty(value = "是否删除(0-正常,1-删除)")
    @TableField("is_deleted")
    private String isDeleted;


}
