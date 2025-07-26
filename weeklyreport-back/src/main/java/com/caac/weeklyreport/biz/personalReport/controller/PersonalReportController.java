package com.caac.weeklyreport.biz.personalReport.controller;

import com.caac.weeklyreport.biz.personalReport.entity.vo.PersonalReportVO;
import com.caac.weeklyreport.biz.personalReport.service.IPersonalReportService;
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
 * 个人周报表 前端控制器
 * </p>
 *
 * @author hanrenjie
 * @since 2025-06-05
 */
@RestController
@RequestMapping("/api/v1/personal-report")
@Api(value = "个人周报管理", tags = "个人周报管理")
public class PersonalReportController {

    private final IPersonalReportService personalReportService;

    public PersonalReportController(IPersonalReportService personalReportService) {
        this.personalReportService = personalReportService;
    }

    @ApiOperation(value = "保存为草稿", notes = "保存为草稿")
    @PostMapping("/saveDraft")
    public ResultBean<?> savePersonalReportDraft(@RequestBody PersonalReportVO personalReport) {
        if (StringUtils.isEmpty(personalReport.getUserId()) || personalReport.getWeek() == null) {
            return ResultBean.fail(ResultCode.PARAM_IS_NULL);
        }
        return ResultBean.success(personalReportService.savePersonalReportDraft(personalReport));
    }

    @ApiOperation(value = "提交", notes = "提交")
    @PostMapping("/submit")
    public ResultBean<?> submitPersonalReport(@RequestBody PersonalReportVO personalReport) {
        if (StringUtils.isEmpty(personalReport.getUserId()) || personalReport.getWeek() == null) {
            return ResultBean.fail(ResultCode.PARAM_IS_NULL);
        }
        return ResultBean.success(personalReportService.submitPersonalReport(personalReport));
    }

    @ApiOperation(value = "员工周报填写-获取当前状态和周报数据", notes = "员工周报填写-获取当前状态和周报数据")
    @GetMapping("/getCurrentStatusAndWeeklyReport/{year}/{week}")
    public ResultBean<?> getCurrentStatusAndWeeklyReport(@PathVariable int year, @PathVariable int week) {
        return ResultBean.success(personalReportService.getCurrentStatusAndWeeklyReport(year,week));
    }


    @ApiOperation(value = "根据年份和周数获取当前状态和周报数据", notes = "根据年份和周数获取当前状态和周报数据")
    @GetMapping("/getWeeklyReportByTime/{year}/{week}")
    public ResultBean<?> getWeeklyReportByTime(@PathVariable int year, @PathVariable int week) {
        return ResultBean.success(personalReportService.getWeeklyReportByTime(year,week));
    }

    @ApiOperation(value = "根据年份和周数获取导出周报数据", notes = "根据年份和周数获取导出周报数据")
    @PassToken
    @GetMapping("/exportPersonalReportExcel/{teamId}/{week}/{year}")
    public void exportPersonalReportExcel(@PathVariable String teamId, @PathVariable int week, @PathVariable int year, HttpServletResponse response) {
        personalReportService.exportPersonalReportExcel(teamId,week,year,response);
    }

}
