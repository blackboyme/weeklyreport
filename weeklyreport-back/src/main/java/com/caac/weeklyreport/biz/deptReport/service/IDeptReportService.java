package com.caac.weeklyreport.biz.deptReport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.biz.deptReport.entity.DeptReport;
import com.caac.weeklyreport.biz.deptReport.entity.dto.DeptReportWeekDTO;
import com.caac.weeklyreport.biz.deptReport.entity.vo.DeptReportVO;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 部门周报表 服务类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
public interface IDeptReportService extends IService<DeptReport> {
    DeptReport saveDeptReportDraft(DeptReportVO deptReport);
    DeptReport saveDeptReport(DeptReportVO deptReportVO);
    DeptReport getWeeklyReportByTime(int year, int week);
    DeptReportWeekDTO getCurrentStatusAndDeptReport(int year, int week);
    void exportDeptReportExcel(String deptId, int startWeek, int endWeek,int year, HttpServletResponse response);
//    DeptReport createDeptReport(DeptReport deptReport);
//    DeptReport getDeptReportById(String id);
//    List<DeptReport> getAllDeptReports();
//    DeptReport updateDeptReport(DeptReport deptReport);
//    void deleteDeptReport(String id);
//    DeptReport getLastWeeklyReportByTime(int year, int week);
//    DeptReport getDeptDraftByUserIdAndWeek(String userId, int week,int year);

}
