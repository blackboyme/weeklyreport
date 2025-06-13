package com.caac.weeklyreport.controller;

import com.caac.weeklyreport.common.ResultBean;
import com.caac.weeklyreport.common.ResultCode;
import com.caac.weeklyreport.entity.DeptReport;
import com.caac.weeklyreport.entity.vo.DeptReportVO;
import com.caac.weeklyreport.service.IDeptReportService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 部门周报表 前端控制器
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@RestController
@RequestMapping("/api/v1/dept-report")
public class DeptReportController {
    
    private final IDeptReportService deptReportService;

    public DeptReportController(IDeptReportService deptReportService) {
        this.deptReportService = deptReportService;
    }

    @PostMapping
    public ResponseEntity<DeptReport> createDeptReport(@RequestBody DeptReport deptReport) {
        return ResponseEntity.ok(deptReportService.createDeptReport(deptReport));
    }

    @ApiOperation(value = "根据年份和周数获取当前状态和周报数据", notes = "根据年份和周数获取当前状态和周报数据")
    @GetMapping("/getWeeklyReportByTime/{year}/{week}")
    public ResultBean<?> getWeeklyReportByTime(@PathVariable int year, @PathVariable int week) {
        return ResultBean.success(deptReportService.getWeeklyReportByTime(year, week));
    }

    @ApiOperation(value = "部门周报保存为草稿", notes = "部门周报保存为草稿")
    @PostMapping("/saveDraft")
    public ResultBean<?> saveDeptReportDraft(@RequestBody DeptReportVO deptReport) {
        if (StringUtils.isEmpty(deptReport.getDeptId()) || deptReport.getWeek() == null) {
            return ResultBean.fail(ResultCode.PARAM_IS_NULL);
        }
        return ResultBean.success(deptReportService.saveDeptReportDraft(deptReport));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeptReport> getDeptReport(@PathVariable String id) {
        DeptReport deptReport = deptReportService.getDeptReportById(id);
        return deptReport != null ? ResponseEntity.ok(deptReport) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<DeptReport>> getAllDeptReports() {
        return ResponseEntity.ok(deptReportService.getAllDeptReports());
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeptReport> updateDeptReport(@PathVariable String id, @RequestBody DeptReport deptReport) {
        deptReport.setDeptId(id);
        return ResponseEntity.ok(deptReportService.updateDeptReport(deptReport));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeptReport(@PathVariable String id) {
        deptReportService.deleteDeptReport(id);
        return ResponseEntity.ok().build();
    }
}
