package com.caac.weeklyreport.biz.personalReport.controller;

import com.caac.weeklyreport.biz.personalReport.entity.vo.PersonalReportVO;
import com.caac.weeklyreport.biz.personalReport.service.IPersonalReportService;
import com.caac.weeklyreport.common.ResultBean;
import com.caac.weeklyreport.common.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/getCurrentStatusAndWeeklyReport")
    public ResultBean<?> getCurrentStatusAndWeeklyReport() {
        return ResultBean.success(personalReportService.getCurrentStatusAndWeeklyReport());
    }


    @ApiOperation(value = "根据年份和周数获取当前状态和周报数据", notes = "根据年份和周数获取当前状态和周报数据")
    @GetMapping("/getWeeklyReportByTime/{year}/{week}")
    public ResultBean<?> getWeeklyReportByTime(@PathVariable int year, @PathVariable int week) {
        return ResultBean.success(personalReportService.getWeeklyReportByTime(year,week));
    }

//    @ApiOperation(value = "根据状态团队领导查询所有团队成员周报", notes = "根据状态团队领导查询所有团队成员周报")
//    @PostMapping("/approveList")
//    public ResultBean<?> getAllPersonalReportByStatus(@RequestBody StatusVO statusVO) {
//        List<PersonalReportStatusDTO> allPersonalReportsForWeek = personalReportService.getAllPersonalReportByStatus(statusVO.getStatus());
//        return ResultBean.success(allPersonalReportsForWeek);
//    }
//
//    @ApiOperation(value = "领导工作日志查询专用-根据年份和周数获取当前状态和周报数据", notes = "领导工作日志查询专用-根据年份和周数获取当前状态和周报数据")
//    @GetMapping("/getLeaderWeeklyReportByTime/{year}/{week}")
//    public ResultBean<?> getLeaderWeeklyReportByTime(@PathVariable int year, @PathVariable int week) {
//        return ResultBean.success(personalReportService.getLeaderWeeklyReportByTime(year,week));
//    }
//

//
//    @ApiOperation(value = "审核通过", notes = "审核通过")
//    @PostMapping("/pass")
//    public ResultBean<?> passPersonalReport(@RequestBody PassVO passVO) {
//        if (StringUtils.isEmpty(passVO.getPrId())) {
//            return ResultBean.fail(ResultCode.PARAM_IS_NULL);
//        }
//        return ResultBean.success(personalReportService.passPersonalReport(passVO));
//    }
//
//    @ApiOperation(value = "退回", notes = "退回")
//    @PostMapping("/cancel")
//    public ResultBean<?> cancelPersonalReport(@RequestBody CancelVO cancelVO) {
//        if (StringUtils.isEmpty(cancelVO.getPrId())) {
//            return ResultBean.fail(ResultCode.PARAM_IS_NULL);
//        }
//        return ResultBean.success(personalReportService.cancelPersonalReport(cancelVO));
//    }
//
//
//
//    @GetMapping("/{id}")
//    public ResponseEntity<PersonalReport> getPersonalReport(@PathVariable String id) {
//        PersonalReport personalReport = personalReportService.getPersonalReportById(id);
//        return personalReport != null ? ResponseEntity.ok(personalReport) : ResponseEntity.notFound().build();
//    }
//
//    @GetMapping
//    public ResponseEntity<List<PersonalReport>> getAllPersonalReports() {
//        return ResponseEntity.ok(personalReportService.getAllPersonalReports());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<PersonalReport> updatePersonalReport(@PathVariable String id, @RequestBody PersonalReport personalReport) {
//        personalReport.setPrId(id);
//        return ResponseEntity.ok(personalReportService.updatePersonalReport(personalReport));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deletePersonalReport(@PathVariable String id) {
//        personalReportService.deletePersonalReport(id);
//        return ResponseEntity.ok().build();
//    }
//
//    @PassToken
//    @GetMapping("/exportPersonalReportExcel/{teamId}/{week}/{year}")
//    public void exportPersonalReportExcel(@PathVariable String teamId,@PathVariable int week,@PathVariable int year,HttpServletResponse response) {
//        personalReportService.exportPersonalReportExcel(teamId,week,year,response);
//    }
}
