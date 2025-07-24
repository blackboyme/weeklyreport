package com.caac.weeklyreport.biz.deptReport.entity.dto;

import com.caac.weeklyreport.biz.deptReport.entity.DeptReport;
import com.caac.weeklyreport.biz.teamReport.entity.TeamReport;
import lombok.Data;

import java.util.List;


@Data
public class DeptReportWeekDTO {
    DeptReport currentWeekDeptReport;
    List<TeamReport> teamReports;
    String currentStatus;
    Boolean canOperate;
}
