package com.caac.weeklyreport.biz.deptReport.controller;

import com.caac.weeklyreport.biz.deptReport.entity.vo.DeptReportVO;
import com.caac.weeklyreport.biz.deptReport.service.IDeptReportService;
import com.caac.weeklyreport.common.PassToken;
import com.caac.weeklyreport.common.ResultBean;
import com.caac.weeklyreport.common.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

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
@Api(value = "部门周报管理",tags="部门周报管理")
public class DeptReportController {

    private final IDeptReportService deptReportService;

    public DeptReportController(IDeptReportService deptReportService) {
        this.deptReportService = deptReportService;
    }

    @ApiOperation(value = "部门周报保存为草稿", notes = "部门周报保存为草稿")
    @PostMapping("/saveDraft")
    public ResultBean<?> saveDeptReportDraft(@RequestBody DeptReportVO deptReport) {
        if (StringUtils.isEmpty(deptReport.getDeptId()) || deptReport.getWeek() == null) {
            return ResultBean.fail(ResultCode.PARAM_IS_NULL);
        }
        return ResultBean.success(deptReportService.saveDeptReportDraft(deptReport));
    }

    @ApiOperation(value = "部门周报正式保存", notes = "部门周报正式保存")
    @PostMapping("/submit")
    public ResultBean<?> saveDeptReport(@RequestBody DeptReportVO deptReport) {
        if (StringUtils.isEmpty(deptReport.getDeptId()) || deptReport.getWeek() == null) {
            return ResultBean.fail(ResultCode.PARAM_IS_NULL);
        }
        return ResultBean.success(deptReportService.saveDeptReport(deptReport));
    }


    @ApiOperation(value = "部门周报填写-获取当前状态和周报数据", notes = "部门周报填写-获取当前状态和周报数据")
    @GetMapping("/getCurrentStatusAndWeeklyReport/{year}/{week}")
    public ResultBean<?> getCurrentStatusAndWeeklyReport(@PathVariable int year, @PathVariable int week) {
        return ResultBean.success(deptReportService.getCurrentStatusAndDeptReport(year, week));
    }

    @ApiOperation(value = "根据年份和周数获取当前状态和周报数据", notes = "根据年份和周数获取当前状态和周报数据")
    @GetMapping("/getWeeklyReportByTime/{year}/{week}")
    public ResultBean<?> getWeeklyReportByTime(@PathVariable int year, @PathVariable int week) {
        return ResultBean.success(deptReportService.getWeeklyReportByTime(year, week));
    }

    @ApiOperation(value = "根据年份和周数获取导出周报数据", notes = "根据年份和周数获取导出周报数据")
    @PassToken
    @GetMapping("/exportTeamReportExcel/{deptId}/{startWeek}/{endWeek}/{year}")
    public void exportTeamReportExcel(@PathVariable String deptId, @PathVariable int startWeek, @PathVariable int endWeek, @PathVariable int year, HttpServletResponse response) {
        deptReportService.exportDeptReportExcel(deptId,startWeek,endWeek,year,response);
    }


}
