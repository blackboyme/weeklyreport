package com.caac.weeklyreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.entity.TeamReport;
import com.caac.weeklyreport.entity.dto.PersonalReportWeekDTO;
import com.caac.weeklyreport.entity.dto.TeamReportWeekDTO;

import java.util.List;

/**
 * <p>
 * 团队周报表 服务类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
public interface ITeamReportService extends IService<TeamReport> {
    TeamReport createTeamReport(TeamReport teamReport);
    TeamReport getTeamReportById(String id);
    List<TeamReport> getAllTeamReports();
    TeamReport updateTeamReport(TeamReport teamReport);
    void deleteTeamReport(String id);
    TeamReport saveTeamReportDraft(TeamReport teamReport);
    TeamReport getTeamDraftByUserIdAndWeek(String userId, int week,int year);
    TeamReport submitTeamReport(TeamReport teamReport);
    TeamReportWeekDTO getCurrentStatusAndWeeklyReport();

}
