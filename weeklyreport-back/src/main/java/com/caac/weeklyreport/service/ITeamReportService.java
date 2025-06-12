package com.caac.weeklyreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.entity.TeamReport;
import com.caac.weeklyreport.entity.dto.TeamReportWeekDTO;
import com.caac.weeklyreport.entity.vo.TeamReportVO;

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
    TeamReport saveTeamReportDraft(TeamReportVO teamReportVO);
    TeamReport getTeamDraftByUserIdAndWeek(String userId, int week,int year);
    TeamReport submitTeamReport(TeamReportVO teamReport);
    TeamReportWeekDTO getCurrentStatusAndWeeklyReport();

}
