package com.caac.weeklyreport.biz.personalReport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.biz.personalReport.entity.PersonalReport;
import com.caac.weeklyreport.biz.personalReport.entity.dto.PersonalReportWeekDTO;
import com.caac.weeklyreport.biz.personalReport.entity.vo.PersonalReportVO;

import javax.servlet.http.HttpServletResponse;


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
    PersonalReportWeekDTO getCurrentStatusAndWeeklyReport(int year, int week);
    PersonalReport getWeeklyReportByTime(int year, int week);
    void exportPersonalReportExcel(String teamId, int startTime, int endTIme, HttpServletResponse response);

}
