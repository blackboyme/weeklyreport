package com.caac.weeklyreport.biz.personalReport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.biz.personalReport.entity.PersonalReport;
import com.caac.weeklyreport.biz.personalReport.entity.dto.PersonalReportWeekDTO;
import com.caac.weeklyreport.biz.personalReport.entity.vo.PersonalReportVO;


/**
 * <p>
 * 个人周报表 服务类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-07-24
 */
public interface IPersonalReportService extends IService<PersonalReport> {
    PersonalReport savePersonalReportDraft(PersonalReportVO personalReport);
    PersonalReport submitPersonalReport(PersonalReportVO personalReport);
    PersonalReportWeekDTO getCurrentStatusAndWeeklyReport();
    PersonalReport getWeeklyReportByTime(int year, int week);
    //    PersonalReport updatePersonalReport(PersonalReport personalReport);
//    List<PersonalReport> getAllPersonalReports();
//    PersonalReport getPersonalReportById(String id);
//    PersonalReport createPersonalReport(PersonalReport personalReport);
//    void deletePersonalReport(String id);
//    PersonalReport getDraftByUserIdAndWeek(String userId, int week, int year);
//    ResultBean<PersonalReport> frontPersonalReportCheck(String userId, int week);
//    List<PersonalReport> getAllPersonalReportsForWeek(String userId, int year, int week);
//    List<PersonalReportStatusDTO> getAllPersonalReportByStatus(String status);

//    Boolean passPersonalReport(PassVO passVO);
//    Boolean cancelPersonalReport(CancelVO cancelVO);
//    PersonalReportStatusDTO getLeaderWeeklyReportByTime(int year, int week);
//    void exportPersonalReportExcel(String teamId, int startTime, int endTIme, HttpServletResponse response);
}
