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
 * 流程记录表
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("flow_record")
@ApiModel(value="FlowRecord对象", description="流程记录表")
public class    FlowRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "流程ID")
    @TableId(value = "flow_id", type = IdType.NONE)
    private String flowId;

    @ApiModelProperty(value = "周报ID")
    @TableField("report_id")
    private String reportId;

    @ApiModelProperty(value = "周报类型：1-个人周报,2-团队周报,3-部门周报")
    @TableField("report_type")
    private String reportType;

    @ApiModelProperty(value = "当前状态：1-草稿,2-待审核,3-已审核,4-已退回")
    @TableField("current_status")
    private String currentStatus;

    @ApiModelProperty(value = "当前环节：1-员工提交,2-团队审核,3-部门审核")
    @TableField("current_stage")
    private String currentStage;

    @ApiModelProperty(value = "提交人ID")
    @TableField("submitter_id")
    private String submitterId;

    @ApiModelProperty(value = "提交人名称")
    @TableField("submitter_name")
    private String submitterName;

    @ApiModelProperty(value = "当前审批人ID")
    @TableField("approver_id")
    private String approverId;

    @ApiModelProperty(value = "当前审批人名称")
    @TableField("approver_name")
    private String approverName;

    @ApiModelProperty(value = "审批意见")
    @TableField("comment")
    private String comment;

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
