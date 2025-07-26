package com.caac.weeklyreport.biz.teamReport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.biz.teamReport.entity.TeamReport;
import com.caac.weeklyreport.biz.teamReport.entity.dto.TeamReportWeekDTO;
import com.caac.weeklyreport.biz.teamReport.entity.vo.TeamReportVO;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 团队周报表 服务类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
public interface ITeamReportService extends IService<TeamReport> {
    TeamReport saveTeamReportDraft(TeamReportVO teamReportVO);
    TeamReport submitTeamReport(TeamReportVO teamReport);
    TeamReportWeekDTO getCurrentStatusAndWeeklyReport(int year, int week);
    TeamReport getWeeklyReportByTime(int year, int week, String teamId);
    void exportTeamReportExcel(String teamId, int startWeek, int endWeek,int year, HttpServletResponse response);
//    TeamReport createTeamReport(TeamReport teamReport);
//    TeamReport getTeamReportById(String id);
//    List<TeamReport> getAllTeamReports();
//    TeamReport updateTeamReport(TeamReport teamReport);
//    void deleteTeamReport(String id);
//    TeamReport getTeamDraftByUserIdAndWeek(String userId, int week,int year);
//    List<TeamReportStatusDTO> getAllTeamReportByStatus(String status);
//    Boolean passTeamReport(PassVO passVO);
//    Boolean cancelTeamReport(CancelVO cancelVO);
}
