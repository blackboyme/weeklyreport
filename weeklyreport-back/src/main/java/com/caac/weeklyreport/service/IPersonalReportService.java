package com.caac.weeklyreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.common.ResultBean;
import com.caac.weeklyreport.entity.PersonalReport;
import com.caac.weeklyreport.entity.dto.StatusPersonalReportDTO;
import com.caac.weeklyreport.entity.vo.PersonalReportVO;

import java.util.List;

/**
 * <p>
 * 个人周报表 服务类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
public interface IPersonalReportService extends IService<PersonalReport> {
    PersonalReport createPersonalReport(PersonalReport personalReport);
    PersonalReport getPersonalReportById(String id);
    List<PersonalReport> getAllPersonalReports();
    PersonalReport updatePersonalReport(PersonalReport personalReport);
    void deletePersonalReport(String id);
    PersonalReport savePersonalReportDraft(PersonalReportVO personalReport);
    PersonalReport submitPersonalReport(PersonalReportVO personalReport);
    PersonalReport getDraftByUserIdAndWeek(String userId, int week,int year);
    ResultBean<PersonalReport> frontPersonalReportCheck(String userId, int week);
    List<PersonalReport> getAllPersonalReportsForWeek(String userId, int year, int week);
    StatusPersonalReportDTO getCurrentStatusAndWeeklyReport();
}
