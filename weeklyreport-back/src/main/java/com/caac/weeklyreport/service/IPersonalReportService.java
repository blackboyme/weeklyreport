package com.caac.weeklyreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.entity.PersonalReport;

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
    PersonalReport savePersonalReportDraft(PersonalReport personalReport);
    PersonalReport getDraftByUserIdAndWeek(String userId, int week);
}
