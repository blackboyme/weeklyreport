package com.caac.weeklyreport.biz.teamReport.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
public class TeamReportVO {

    @TableField("team_id")
    private String teamId;

    @TableField("user_id")
    private String userId;

    @ApiModelProperty(value = "周数")
    @TableField("week")
    private Integer week;

    @ApiModelProperty(value = "年")
    @TableField("year")
    private Integer year;

    @ApiModelProperty(value = "本周主要工作")
    @TableField("summary")
    private String summary;

    @ApiModelProperty(value = "本周主要工作")
    @TableField("next_summary")
    private String nextSummary;
}
