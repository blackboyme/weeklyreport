package com.caac.weeklyreport.biz.teamReport.entity.dto;

import com.caac.weeklyreport.biz.personalReport.entity.PersonalReport;
import com.caac.weeklyreport.biz.teamReport.entity.TeamReport;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="周报填写返回类", description="周报填写返回类")
public class TeamReportWeekDTO {
    TeamReport currentWeekTeamReport;
    TeamReport lastWeekTeamReport;
    PersonalReport currentWeekPersonalReport;
    String currentStatus;
    Boolean canOperate;
}
