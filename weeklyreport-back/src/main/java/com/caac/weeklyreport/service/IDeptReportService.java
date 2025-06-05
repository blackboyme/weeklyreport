package com.caac.weeklyreport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.caac.weeklyreport.entity.DeptReport;

import java.util.List;

/**
 * <p>
 * 部门周报表 服务类
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
public interface IDeptReportService extends IService<DeptReport> {
    DeptReport createDeptReport(DeptReport deptReport);
    DeptReport getDeptReportById(String id);
    List<DeptReport> getAllDeptReports();
    DeptReport updateDeptReport(DeptReport deptReport);
    void deleteDeptReport(String id);
}
