package com.caac.weeklyreport.biz.personalReport.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PersonalReportVO {
    /**
     * 员工ID
     */
    @ApiModelProperty(value = "员工ID")
    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "周数")
    @TableField("week")
    private Integer week;

    @ApiModelProperty(value = "年")
    @TableField("year")
    private Integer year;

    @ApiModelProperty(value = "本周主要工作")
    @TableField("major")
    private String major;

    @ApiModelProperty(value = "本周主要工作特殊说明")
    @TableField("special_major")
    private String specialMajor;

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
    @TableField("next_major")
    private String nextMajor;

    @ApiModelProperty(value = "下周主要工作特殊说明")
    @TableField("next_special_major")
    private String nextSpecialMajor;

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
}
